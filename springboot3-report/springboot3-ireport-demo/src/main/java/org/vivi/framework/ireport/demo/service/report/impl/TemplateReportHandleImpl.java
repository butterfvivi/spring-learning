package org.vivi.framework.ireport.demo.service.report.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.service.report.ReportHandleStrategy;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

@Service
public class TemplateReportHandleImpl implements ReportHandleStrategy {
    @Override
    public String target() {
        return "template";
    }

    @Override
    public void handleExport(HttpServletResponse response, GenerateReportDto reportDto) {

    }

}
