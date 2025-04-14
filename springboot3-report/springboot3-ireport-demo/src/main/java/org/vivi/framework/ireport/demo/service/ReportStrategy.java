package org.vivi.framework.ireport.demo.service;

import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

public interface ReportStrategy {

    String type();

    void generateReport(GenerateReportDto reportDto);
}
