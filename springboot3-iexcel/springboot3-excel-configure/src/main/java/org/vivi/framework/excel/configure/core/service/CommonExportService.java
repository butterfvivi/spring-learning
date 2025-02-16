package org.vivi.framework.excel.configure.core.service;

import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;

import java.util.List;

/**
 *  公共业务报表导出据组件对外接口定义
 */
public interface CommonExportService {

    List getExportData(ExportBeanConfig exportBeanConfig) throws Exception;
}
