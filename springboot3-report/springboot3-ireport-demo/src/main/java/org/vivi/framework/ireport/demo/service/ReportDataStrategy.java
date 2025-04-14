package org.vivi.framework.ireport.demo.service;


import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

public interface ReportDataStrategy {

    /**
     * 类型
     */
    String type();

    /**
     * data transform
     */
    IDynamicExportDto transform(GenerateReportDto reportDto, IDynamicExportDto exportDto);
}
