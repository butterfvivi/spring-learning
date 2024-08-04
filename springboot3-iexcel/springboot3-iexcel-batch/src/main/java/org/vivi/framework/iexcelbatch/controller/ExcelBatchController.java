package org.vivi.framework.iexcelbatch.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;
import org.vivi.framework.iexcelbatch.service.BatchDataService;
import org.vivi.framework.iexcelbatch.service.ExcelHandleService;
import org.vivi.framework.iexcelbatch.service.UserMultiSheetService;

@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelBatchController {

    @Autowired
    private BatchDataService batchDataService;

    @Autowired
    private ExcelHandleService excelHandleService;

    @Autowired
    private UserMultiSheetService userMultiSheetService;

    @Autowired
    private Environment env;

    @PostMapping(value = "/import")
    public R<Boolean>  importExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) {
        return R.success(excelHandleService.importExcel(request, response, file));
    }

    /**
     * 批量生成百万级别数据
     *
     * @return
     */
    @PostMapping(value = "/batch")
    @ResponseBody
    public R batchInsertData(@RequestParam Integer times) {
        try {
            //times为批量插入数据的次数，每次数据量为 Constant.batchDataSize
            batchDataService.batchUsers(times);
        } catch (Exception e) {
            log.error("预批量生成百万级别数据-发生异常：{}", e.getMessage());
            return  R.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return R.success();
    }

    @ApiOperation(value = "分批导出-单sheet页")
    @PostMapping("/v1/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody UserQuery query) {
        excelHandleService.exportExcel(request, response, query);
    }

    @ApiOperation(value = "分批导出-多sheet页")
    @PostMapping("/v2/export")
    public void exportMultiExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody UserQuery query) {
        userMultiSheetService.exportExcel(request, response, query);
    }
}
