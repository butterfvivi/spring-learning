package org.vivi.framework.iexcelbatch.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.iexcelbatch.entity.dto.CellSelectDataPair;
import org.vivi.framework.iexcelbatch.entity.dto.CellSelectDto;
import org.vivi.framework.iexcelbatch.entity.dto.ModelDto;
import org.vivi.framework.iexcelbatch.listener.CustomBatchSheetWriteHandler;
import org.vivi.framework.iexcelbatch.listener.CustomSheetWriteHandler;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/select")
public class ExcelSelectController {

    private static final List<String> sexList = Arrays.asList("男", "女", "未知");
    private static final List<String> cityList = Arrays.asList("深圳", "广州", "上海", "北京", "杭州");

    @Operation(summary = "sheet设置单列的下拉值测试接口")
    @GetMapping("/v1/export")
    public void tempalte(HttpServletResponse response) throws Exception {

        // 通用内容设置
        String fileName = URLEncoder.encode("tempalte.xlsx", CharEncoding.UTF_8);
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        // 构建模板数据
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet writeSheet = EasyExcel
                .writerSheet(0, "用户信息")
                .registerWriteHandler(new CustomSheetWriteHandler(new CellSelectDto(1, 100, 2, 2), cityList))
                .head(ModelDto.class)
                .build();
        ExcelWriter write = excelWriter.write(Arrays.asList(new ModelDto("张三", "男", "深圳")), writeSheet);
        write.finish();
    }

    @Operation(summary = "sheet设置多列的下拉值测试接口")
    @GetMapping("/v2/export")
    public void tempalteBatch(HttpServletResponse response) throws Exception {

        // 通用内容设置
        String fileName = URLEncoder.encode("tempalteBatch.xlsx", CharEncoding.UTF_8);
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        // 构建模板数据
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

        //只需要填写一个cellSelectVO对象从哪个格子开始到哪里结束及数据就能生成下拉的值
        List<CellSelectDataPair> dataPairs = Lists.newArrayList(
                new CellSelectDataPair(new CellSelectDto(1, 100, 2), cityList),
                new CellSelectDataPair(new CellSelectDto(1, 100, 1), sexList));


        // 根据用户传入字段 假设我们只要导出 date
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("name");
        includeColumnFiledNames.add("sex");


        WriteSheet writeSheet = EasyExcel
                .writerSheet(0, "用户信息")
                .registerWriteHandler(new CustomBatchSheetWriteHandler(dataPairs))
                .includeColumnFiledNames(includeColumnFiledNames)
                .head(ModelDto.class)
                .build();
        ExcelWriter write = excelWriter.write(Arrays.asList(new ModelDto("张三", "男", "深圳")), writeSheet);
        write.finish();
    }
}
