package org.vivi.framework.excel.dynamic4.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.excel.dynamic4.model.ExportParam;
import org.vivi.framework.excel.dynamic4.service.IEasyExcelService;
import org.vivi.framework.excel.common.exception.CommonException;

@RestController
@RequestMapping("/excel4")
public class ExcelController4 {

    @Autowired
    private IEasyExcelService exportService;

    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ExportParam param) throws CommonException {
        //exportService.exportExcel(response, param);
    }


}
