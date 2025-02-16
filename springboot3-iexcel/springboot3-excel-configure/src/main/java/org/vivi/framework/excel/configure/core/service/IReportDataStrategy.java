package org.vivi.framework.excel.configure.core.service;

import org.springframework.stereotype.Component;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;

@Component
public interface IReportDataStrategy {

    /**
     * 报表导出策略接口
     * @param exportBean
     * @return
     */
    Object exeExport(ExportBeanConfig exportBean) throws Exception;
}
