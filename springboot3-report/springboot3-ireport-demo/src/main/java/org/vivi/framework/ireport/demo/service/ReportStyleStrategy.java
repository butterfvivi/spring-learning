package org.vivi.framework.ireport.demo.service;

import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

public interface ReportStyleStrategy {

    /**
     * 类型
     */
    String type();

    /**
     * report set cell style
     */
    void setStyle( IDynamicExportDto exportDto);
}
