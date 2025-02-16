package org.vivi.framework.excel.configure.core.service.impl;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;
import org.vivi.framework.excel.configure.core.service.IReportDataStrategy;
import org.vivi.framework.excel.configure.core.service.CommonExportService;

import java.util.List;
import java.util.Map;

/**
 * 公共业务报表导出据组件对外服务入口
 */
@Service
public class CommonExportServiceImpl implements CommonExportService {
    private Logger logger = LoggerFactory.getLogger(CommonExportServiceImpl.class);

    @Resource(name = "reportDataListMapStrategy")
    private IReportDataStrategy reportDataListMapStrategy;

    @Resource(name = "reportDataListObjectStrategy")
    private IReportDataStrategy reportDataListObjectStrategy;

    @Override
    public List getExportData(ExportBeanConfig exportBeanConfig) throws Exception {

        long startTime = System.currentTimeMillis();
        if(!exportBeanConfig.isUseObjectModel()){
            List<Map<Integer, String>> resultList = (List<Map<Integer, String>>)reportDataListMapStrategy.exeExport(exportBeanConfig);
            long endTime = System.currentTimeMillis();
            logger.info("use dataListMap model  report convert use time = {}",(endTime - startTime));
            return resultList;

        }else {
            List list = (List)reportDataListObjectStrategy.exeExport(exportBeanConfig);
            long endTime = System.currentTimeMillis();
            logger.info("use dataListObject model report convert use time = {}",(endTime - startTime));
            return list;
        }

    }
}
