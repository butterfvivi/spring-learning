package org.vivi.framework.easyexcel.simple.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.easyexcel.simple.service.TemplateReportService;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateReportService templateReportService;

    @GetMapping(value = "/export")
    public void export(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        templateReportService.export(httpServletRequest, httpServletResponse);
    }


}
