package org.vivi.framework.iexcelbatch.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.service.BatchDataService;
import org.vivi.framework.iexcelbatch.service.ExcelHandleService;

@Slf4j
@RestController
@RequestMapping
public class ExcelImportController {

    @Autowired
    private BatchDataService batchDataService;

    @Autowired
    private ExcelHandleService excelHandleService;

    @RequestMapping(value = "/excel/importExcel")
    public R<Boolean>  importExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) {
        return R.success(excelHandleService.importExcel(request, response, file));
    }

    /**
     * 批量生成百万级别数据
     *
     * @return
     */
    @RequestMapping(value = "/batch/insert")
    @ResponseBody
    public R batchInsertData(@RequestParam Integer times) {
        try {
            //times为批量插入数据的次数，每次数据量为 Constant.batchDataSize
            batchDataService.batchInitItem(times);
        } catch (Exception e) {
            log.error("预批量生成百万级别数据-发生异常：{}", e.getMessage());
            return  R.success(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return R.success();
    }

}
