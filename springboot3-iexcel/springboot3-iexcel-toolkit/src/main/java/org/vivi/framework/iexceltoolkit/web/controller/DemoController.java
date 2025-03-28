package org.vivi.framework.iexceltoolkit.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.iexceltoolkit.toolkit.core.ExcelInvoke;
import org.vivi.framework.iexceltoolkit.web.DataGenerator;
import org.vivi.framework.iexceltoolkit.web.request.ITemplateExportReq;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/test")
public class DemoController {

    @PostMapping("/template/export")
    public void templateExport(HttpServletResponse response, @RequestBody ITemplateExportReq dto) throws Exception {
        List<Map<String, Object>> productMaps = new DataGenerator().readJsonFile("json/genProducts.json");
        dto.setDataList(productMaps);
        Map<String, Object> otherVal = new HashMap<>();
        otherVal.put("product", productMaps);
        dto.setOtherVal(otherVal);

        List<Map<String, Object>> projectMaps = new DataGenerator().readJsonFile("json/genProjects.json");
        dto.setDataList(projectMaps);

        ExcelInvoke.templateExport(response, dto);

    }

    /**
     * 导出多个表格，多个表头
     * @param response
     * @param dto
     * @throws Exception
     */
    @PostMapping("/template/export1")
    public void templateExport1(HttpServletResponse response, @RequestBody ITemplateExportReq dto) throws Exception {
        List<Map<String, Object>> productMaps = new DataGenerator().readJsonFile("json/genProducts.json");
        List<Map<String, Object>> projectMaps = new DataGenerator().readJsonFile("json/genProjects.json");

        Map<String, List> mutipleDatas = new HashMap<>();
        mutipleDatas.put("product", productMaps);
        mutipleDatas.put("project", projectMaps);

        dto.setMutipleTableDatas(mutipleDatas);

        ExcelInvoke.templateExport(response, dto);
    }

    @PostMapping("/template/export2")
    public void fillMultipleTables(HttpServletResponse response)  {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ClassPathResource resource = new ClassPathResource("template" + File.separator + "template_mutiple.xlsx");

            List<Map<String, Object>> productMaps = new DataGenerator().readJsonFile("json/genProducts.json");
            List<Map<String, Object>> projectMaps = new DataGenerator().readJsonFile("json/genProjects.json");
            try (ExcelWriter excelWriter = EasyExcel.write(byteArrayOutputStream).withTemplate(resource.getInputStream()).build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet().build();

                // Fill product data
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(new FillWrapper("product", productMaps), fillConfig, writeSheet);

                // Fill project data
                excelWriter.fill(new FillWrapper("project", projectMaps), fillConfig, writeSheet);
            }


            // 这里文件名不起作用是因为前端是写死的
            String fileName = "多列表" + System.currentTimeMillis() + ".xlsx";
            // 设置响应头，告诉浏览器这是一个下载的文件，这里文件名
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // 将文件内容写入响应输出流，浏览器可以直接触发下载
            response.getOutputStream().write(byteArrayOutputStream.toByteArray());
            response.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
