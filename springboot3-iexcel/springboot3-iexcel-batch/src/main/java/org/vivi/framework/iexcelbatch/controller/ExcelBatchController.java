package org.vivi.framework.iexcelbatch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.service.BatchDataService;
import org.vivi.framework.iexcelbatch.service.ExcelBatchService;

@Slf4j
@RestController
@RequestMapping("/batch")
public class ExcelBatchController {

    @Autowired
    private BatchDataService batchDataService;

    private ExcelBatchService excelBatchService;

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


    @PostMapping(value = "/batch")
    @ResponseBody
    public R asyncImport2(@RequestParam Integer times) {
        return R.success();
    }


}
