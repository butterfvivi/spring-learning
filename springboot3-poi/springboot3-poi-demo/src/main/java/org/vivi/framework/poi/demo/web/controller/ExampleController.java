package org.vivi.framework.poi.demo.web.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.poi.demo.model.ExportExcelBean;
import org.vivi.framework.poi.demo.utils.ExportExcelUtil;
import org.vivi.framework.poi.demo.web.vo.ExportExcelTitle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ExampleController {

    @GetMapping("/exportSimpleExcel")
    @Operation(summary = "导出简单的excel")
    public String exportSimpleExcel(HttpServletRequest request, HttpServletResponse response){

        List<Map<String, Object>> list = new ArrayList<>();
        int num = 10;
        for (int i = 0; i < num; i++) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("code", "数据名称" + i);
            map.put("name", "数据编码" + i);
            map.put("brand", "数据品牌" + i);
            list.add(map);
        }

        try {
            String exportFileName = "简单的excel ";
            ExportExcelUtil.updateNameUnicode(request, response, exportFileName);
            OutputStream out = response.getOutputStream();
            //保存导出的excel的名称
            ExportExcelBean table = ExportExcelTitle.table2.getValue();
            table.setDataList(list);
            log.info("导出的数据JSON格式：{}", JSONObject.toJSONString(table));
            ExportExcelUtil.exportExcel(table, out);

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/exportExcelMoreSheetMoreTable")
    @Operation( summary = "导出多sheet，单sheet拥有多表格的excel")
    public String exportExcelMoreSheetMoreTable(HttpServletRequest request, HttpServletResponse response){

        List<ExportExcelBean> excelBean = new ArrayList<>();

        // 模拟数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("name", "名称" + i);
            map.put("name1", "名称" + i);
            map.put("code", "2020051400" + i);
            list.add(map);
        }
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("name", "数据名称" + i);
            map.put("code", "数据名称" + 0);
            map.put("brand", "数据品牌" + i);
            list2.add(map);
        }
        try {
            String exportFileName = "测试数据";
            ExportExcelUtil.updateNameUnicode(request, response, exportFileName);
            OutputStream out = response.getOutputStream();

            // 表格1
            ExportExcelBean table1 = ExportExcelTitle.table1.getValue();
            table1.setDataList(list);

            // 表格2
            ExportExcelBean table2 = ExportExcelTitle.table2.getValue();
            table2.setDataList(list2);

            // sheet1
            ExportExcelBean sheet1 = new ExportExcelBean();

            // sheet1 - 多表格
            List<ExportExcelBean> tables1 = new ArrayList<>();
            tables1.add(table1);
            tables1.add(table2);

            sheet1.setSheetName("测试sheet");
            sheet1.setList(tables1);
            // 设置sheet的密码，仅作用于当前sheet页
            sheet1.setProtectSheet("123456");
            excelBean.add(sheet1);

            // sheet2
            ExportExcelBean sheet2 = new ExportExcelBean();

            // sheet2 - 多表格
            List<ExportExcelBean> tables2 = new ArrayList<>();
            tables2.add(table2);
            tables2.add(table1);

            sheet2.setSheetName("测试sheet2");
            sheet2.setList(tables2);
            excelBean.add(sheet2);

            log.info("导出的数据JSON格式：{}", JSONObject.toJSONString(excelBean));
            ExportExcelUtil.exportExcelMoreSheetMoreTable(excelBean, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}