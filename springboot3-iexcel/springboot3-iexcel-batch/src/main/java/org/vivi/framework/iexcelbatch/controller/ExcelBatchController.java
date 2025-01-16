package org.vivi.framework.iexcelbatch.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;
import org.vivi.framework.iexcelbatch.service.BatchDataService;
import org.vivi.framework.iexcelbatch.service.ExcelBatchService;

/**
 * 异步导入导出 excel数据
 */
@Slf4j
@RestController
@RequestMapping("/batch")
public class ExcelBatchController {

    @Autowired
    private BatchDataService batchDataService;

    @Autowired
    private ExcelBatchService excelBatchService;
    /**
     * 批量生成百万级别数据
     *
     * @return
     */
    @PostMapping(value = "")
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


    @PostMapping(value = "/asyncImport1")
    @ResponseBody
    @Operation(summary = "asyncImport1")
    public R asyncImport1(@RequestParam(value = "file") MultipartFile file) {
        try {
            excelBatchService.asyncImport1(file);
        }catch (Exception e){
            return  R.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return R.success();
    }

    @PostMapping("/asyncImport2")
    @Operation(summary = "asyncImport2")
    public R asyncImport2(@RequestParam(value = "file") MultipartFile file, UserRequest userRequest){
        try {
            return R.success(excelBatchService.asyncImport2(userRequest,file));
        }catch (Exception e){
            return  R.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/asyncImport3")
    @Operation(summary = "asyncImport3")
    public R asyncImport3(@RequestParam(value = "file") MultipartFile file, UserRequest userRequest){
        try {
            excelBatchService.asyncImport3(userRequest,file);
        }catch (Exception e){
            return  R.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return R.success();
    }

    @PostMapping("/asyncbatchImport")
    @Operation(summary = "asyncbatchImport")
    public R asyncbatchImport(@RequestParam(value = "file") MultipartFile[] files, UserRequest userRequest){
        try {
            excelBatchService.asyncbatchImport(userRequest,files);
        }catch (Exception e){
            return  R.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return R.success();
    }

    @PostMapping("/asyncDynamicImport")
    @Operation(summary = "asyncDynamicImport")
    public R asyncDynamicImport(@RequestParam(value = "file") MultipartFile file, UserRequest userRequest){
        try {
            excelBatchService.asyncDynamicImport(file);
        }catch (Exception e){
            return  R.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return R.success();
    }

}
