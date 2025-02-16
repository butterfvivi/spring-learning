package org.vivi.framework.excel.configure.core.service.strategy;

import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;
import org.vivi.framework.excel.configure.core.service.IExport;
import org.vivi.framework.excel.configure.core.service.IMapBatchConverter;
import org.vivi.framework.excel.configure.core.service.IReportDataStrategy;
import org.vivi.framework.excel.configure.core.service.common.ExportConvertService;
import org.vivi.framework.excel.configure.core.service.common.ExportHelperService;
import org.vivi.framework.excel.configure.core.service.common.WriteExcelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component("reportDataListMapStrategy")
public class ReportDataListMapStrategy implements IReportDataStrategy {
    private Logger logger = LoggerFactory.getLogger(ReportDataListMapStrategy.class);

    @Autowired
    private IExport exportService;

    @Autowired
    private ExportHelperService exportHelperService;

    @Resource(name = "defaultMapBatchConverter")
    private IMapBatchConverter defaultMapBatchConverter;

    @Autowired
    private ExportConvertService exportConvertService;

    @Autowired
    private WriteExcelService writeExcelService;

    @Override
    public Object exeExport(ExportBeanConfig exportBean) throws Exception {
        List dataList = exportService.getExportList(exportBean);

        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        List<Map<Integer, String>> resultMapList = new ArrayList<>(dataList.size());
        ConverterFieldBean filterFieldBean = new ConverterFieldBean();
        filterFieldBean.setSourceLinkedMap(exportBean.getQueryCacheMap());
        Set<String> needConvertFieldIndexSet = writeExcelService.getNeedConvertIndexSet(exportBean.getT().getClass());
        filterFieldBean.setSourceLinkedSet(needConvertFieldIndexSet);

        logger.info("filterFieldBean = "+filterFieldBean.toString());

        /**
         * 如果查询出的数据对象列表超过设置的并行查询数据阈值则
         * 使用并行策略进行转换
         */
        Map<String, ExportFieldBean> exportFieldBeanColumnMap = writeExcelService.getMetaFieldColumnMap(exportBean.getT().getClass());

        if (exportBean.getSize() > dataList.size() && exportBean.isUseParallelQuery()) {
            long startTime = System.currentTimeMillis();
            Map<Integer, ExportFieldBean> exportFieldBeanMap = writeExcelService.getMetaFieldMap(exportBean.getT().getClass());
            List<CompletableFuture<List<Map<Integer, String>>>> resultFuture = new ArrayList<>();
            int yu = dataList.size() % exportBean.getLimitCount();
            int page = dataList.size() / exportBean.getLimitCount();
            if (yu != 0) {
                page = page + 1;
            }
            List<Map<Integer, String>> finalResultMapList = resultMapList;

            for (int q = 0; q < page; q++) {
                int start = exportBean.getLimitCount() * q;
                List subDataList;
                if (start + exportBean.getLimitCount() < dataList.size()) {
                    subDataList = dataList.subList(start, start + exportBean.getLimitCount());
                } else {
                    subDataList = dataList.subList(start, dataList.size());
                }
                CompletableFuture<List<Map<Integer,String>>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        //1.将List数据对象转换为List.Map格式
                        List<Map<Integer,String>> rowDataMap = exportHelperService.getConvertRowDataMap(subDataList,exportFieldBeanColumnMap,needConvertFieldIndexSet);
                        //2.执行默认转换器
                        defaultMapBatchConverter.convertBatchFieldData(rowDataMap,exportFieldBeanMap,filterFieldBean);
                        return rowDataMap;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new ArrayList();
                });

                resultFuture.add(future);

                resultFuture.stream().forEach(v -> {
                    try {
                        finalResultMapList.addAll(v.get());
                    } catch (InterruptedException e) {
                        logger.error("获取结果集失败InterruptedException", e);
                    } catch (ExecutionException e) {
                        logger.error("获取结果集失败ExecutionException", e);
                    }
                });
            }

            long endTime = System.currentTimeMillis();
            logger.info("Parallel  List Map  Convert useTime = " + (endTime - startTime));
            //3.执行自定义转换器
            exportConvertService.dealMyconverterResult(finalResultMapList,exportBean,exportFieldBeanMap);
            return finalResultMapList;
        } else {
            long startTime = System.currentTimeMillis();
            Map<Integer, ExportFieldBean> exportFieldBeanMap = writeExcelService.getMetaFieldMap(exportBean.getT().getClass());
            //1.将List数据对象转换为List.Map格式
            resultMapList = exportHelperService.getConvertRowDataMap(dataList,exportFieldBeanColumnMap,needConvertFieldIndexSet);
            //2.执行默认转换器
            defaultMapBatchConverter.convertBatchFieldData(resultMapList,exportFieldBeanMap,filterFieldBean);
            //3.执行自定义转换器
            exportConvertService.dealMyconverterResult(resultMapList,exportBean,exportFieldBeanMap);
            long endTime = System.currentTimeMillis();
            logger.info("converter useTime = " + (endTime - startTime)+",resultMapList = "+resultMapList.size());
            return resultMapList;
        }
    }
}