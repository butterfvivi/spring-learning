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
import org.vivi.framework.excel.configure.core.service.IObjectBatchConverter;
import org.vivi.framework.excel.configure.core.service.IReportDataStrategy;
import org.vivi.framework.excel.configure.core.service.common.ExportConvertService;
import org.vivi.framework.excel.configure.core.service.common.WriteExcelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *  转换为list<map<integer,String>> 对象
 */
@Component("reportDataListObjectStrategy")
public class ReportDataListObjectStrategy implements IReportDataStrategy {
    private Logger logger = LoggerFactory.getLogger(ReportDataListObjectStrategy.class);

    @Autowired
    private IExport exportService;

    @Autowired
    private WriteExcelService writeExcelService;

    @Resource(name = "defaultObjectBatchConverter")
    private IObjectBatchConverter defaultObjectBatchConverter;

    @Autowired
    private ExportConvertService exportConvertService;

    @Override
    public Object exeExport(ExportBeanConfig exportBean) throws Exception {
        List dataList = exportService.getExportList(exportBean);

        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        /**
         * 如果查询出的数据对象列表超过设置的并行查询数据阈值则
         * 使用并行策略进行转换
         */
        List newDataList = new ArrayList();
        ConverterFieldBean filterFieldBean = new ConverterFieldBean();
        filterFieldBean.setSourceLinkedMap(exportBean.getQueryCacheMap());
        Set<String> needConvertFieldIndexSet = writeExcelService.getNeedConvertIndexSet(exportBean.getT().getClass());
        filterFieldBean.setSourceLinkedSet(needConvertFieldIndexSet);

        Map<Integer, ExportFieldBean> exportFieldBeanMap = writeExcelService.getMetaFieldMap(exportBean.getT().getClass());

        if (exportBean.getSize() > dataList.size() && exportBean.isUseParallelQuery()) {
            long startTime = System.currentTimeMillis();
            List<CompletableFuture<List<Object>>> resultFuture = new ArrayList<>();
            int yu = dataList.size() % exportBean.getLimitCount();
            int page = dataList.size() / exportBean.getLimitCount();
            if (yu != 0) {
                page = page + 1;
            }
            for (int q = 0; q < page; q++) {
                int start = exportBean.getLimitCount() * q;
                List subDataList;
                if (start + exportBean.getLimitCount() < dataList.size()) {
                    subDataList = dataList.subList(start, start + exportBean.getLimitCount());
                } else {
                    subDataList = dataList.subList(start, dataList.size());
                }
                CompletableFuture<List<Object>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        defaultObjectBatchConverter.convertBatchFieldData(subDataList,exportFieldBeanMap,filterFieldBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return subDataList;
                });

                resultFuture.add(future);
                resultFuture.stream().forEach(v -> {
                    try {
                        newDataList.addAll(v.get());
                    } catch (InterruptedException e) {
                        logger.error("获取结果集失败InterruptedException", e);
                    } catch (ExecutionException e) {
                        logger.error("获取结果集失败ExecutionException", e);
                    }
                });
            }

            long endTime = System.currentTimeMillis();
            logger.info("Parallel List Object Convert useTime = {}" , (endTime - startTime));
        } else {
            long startTime = System.currentTimeMillis();
            try {
                //1.执行系统默认转换器
                defaultObjectBatchConverter.convertBatchFieldData(dataList,null,filterFieldBean);
            } catch (Exception e) {
                logger.error("执行默认过滤器异常", e);
            }
            long endTime = System.currentTimeMillis();
            logger.info("converter useTime = {}" , (endTime - startTime));
        }
        //2.执行自定义转换器
        exportConvertService.dealMyconverterResult(dataList,exportBean,exportFieldBeanMap);
        return dataList;
    }
}