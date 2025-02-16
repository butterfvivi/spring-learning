package org.vivi.framework.excel.configure.core.service;

import org.apache.poi.ss.formula.functions.T;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;

import java.util.List;

public interface IExport {

    /**
     * 通用导出接口
     * @param exportBean
     * @return
     */
    List<T> getExportList(ExportBeanConfig exportBean) throws Exception;

}
