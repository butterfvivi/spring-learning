package org.vivi.framework.iexcelbatch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;
import org.vivi.framework.iexcelbatch.service.ExcelHandleService;

@Tag(name = "Excel Template")
@RestController
@RequestMapping("/template")
public class ExcelTemplateController {

    @Autowired
    private ExcelHandleService excelHandleService;


    @Operation(summary = "分批导出-单sheet页-固定模板导出")
    @PostMapping("/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody UserQuery query) {
        excelHandleService.exportTemplate(request, response, query);
    }

    @Operation(summary = "导出-动态模板列")
    @PostMapping("/export/dynamic")
    public void exportDynamic(HttpServletRequest request, HttpServletResponse response, @RequestBody UserQuery query) {
        excelHandleService.exportDynamic(request, response,query);
    }

    @Operation(summary = "分批导入-单sheet页-固定模板导入")
    @PostMapping(value = "/import")
    public R<Boolean> importExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) {
        return R.success(excelHandleService.importTemplate(request, response, file));
    }

}
