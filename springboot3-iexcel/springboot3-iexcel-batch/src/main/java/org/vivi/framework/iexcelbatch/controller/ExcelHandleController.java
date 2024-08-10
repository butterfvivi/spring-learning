package org.vivi.framework.iexcelbatch.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;
import org.vivi.framework.iexcelbatch.service.ExcelHandleService;
import org.vivi.framework.iexcelbatch.service.UserMultiSheetService;

@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelHandleController {

    @Autowired
    private ExcelHandleService excelHandleService;

    @Autowired
    private UserMultiSheetService userMultiSheetService;

    @PostMapping(value = "/import")
    public R<Boolean> importExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) {
        return R.success(excelHandleService.importExcel(request, response, file));
    }

    @Operation(summary = "分批导出-单sheet页")
    @PostMapping("/v1/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody UserQuery query) {
        excelHandleService.exportExcel(request, response, query);
    }

    @Operation(summary = "分批导出-多sheet页")
    @PostMapping("/v2/export")
    public void exportMultiExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody UserQuery query) {
        userMultiSheetService.exportExcel(request, response, query);
    }
}
