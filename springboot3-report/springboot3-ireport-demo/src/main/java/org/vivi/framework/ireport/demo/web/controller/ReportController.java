package org.vivi.framework.ireport.demo.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.ireport.demo.common.response.R;
import org.vivi.framework.ireport.demo.service.report.ReportService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.dto.ReportPageDto;

@RequestMapping("/api/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService  reportService;

    @PostMapping("/")
    public R getReportList(){
        return R.success(reportService.getAllReport(null));
    }

    @PostMapping("/page")
    public R getPageReportData(@RequestBody ReportPageDto reportPageDto){
        return R.success(reportService.getPagePreviewData(reportPageDto));
    }

    @PostMapping("/export")
    public void exportReport(HttpServletResponse response,@RequestBody GenerateReportDto reportDto) throws Exception {
        reportService.export(response,reportDto);
    }
}
