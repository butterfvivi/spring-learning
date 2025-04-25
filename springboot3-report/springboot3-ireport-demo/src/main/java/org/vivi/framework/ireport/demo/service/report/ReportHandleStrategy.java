package org.vivi.framework.ireport.demo.service.report;

import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

public interface ReportHandleStrategy {

    String target();

    /**
     * 生成报告
     *
     * @return
     */
    void handleExport(HttpServletResponse response, GenerateReportDto reportDto);

}