package org.vivi.framework.iexceltoolkit.tool.service;

import org.apache.poi.ss.formula.functions.T;
import org.vivi.framework.iexceltoolkit.tool.config.ExportBeanConfig;

import java.util.List;

public interface IExport {

    /**
     * 通用导出接口
     * @param exportBean
     * @return
     */
    List<T> getExportList(ExportBeanConfig exportBean) throws Exception;

}
