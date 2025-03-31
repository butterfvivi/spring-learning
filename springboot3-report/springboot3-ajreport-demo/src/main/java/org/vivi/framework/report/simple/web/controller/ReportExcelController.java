package org.vivi.framework.report.simple.web.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.report.simple.common.response.ResponseBean;
import org.vivi.framework.report.simple.modules.reportexcel.service.ReportExcelService;
import org.vivi.framework.report.simple.web.dto.ReportExcelDto;

import java.io.IOException;


@RestController

@RequestMapping("/reportExcel")
public class ReportExcelController{

    @Autowired
    private ReportExcelService reportExcelService;

    @GetMapping("/detailByReportCode/{reportCode}")
    public ResponseBean detailByReportCode(@PathVariable String reportCode) {
        ReportExcelDto reportExcelDto = reportExcelService.detailByReportCode(reportCode);
        return ResponseBean.builder().data(reportExcelDto).build();
    }

    @PostMapping("/preview")
    public ResponseBean preview(@RequestBody ReportExcelDto reportExcelDto) {
        ReportExcelDto result = reportExcelService.preview(reportExcelDto);
        return ResponseBean.builder().data(result).build();
    }

    @PostMapping("/exportExcel")
    public ResponseEntity<byte[]> export(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportExcelDto reportExcelDto) throws IOException {
        return reportExcelService.exportExcel(request, response, reportExcelDto);
    }
}
