package org.vivi.framework.ireport.demo.service;


import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

public interface IReportDataStrategy {

    /**
     * 类型
     */
    String type();

    /**
     * data transform
     */
    IDynamicExportDto transform(IDynamicExportDto req);
}
