package org.vivi.framework.iexceltoolkit.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.iexceltoolkit.toolkit.core.ExcelInvoke;
import org.vivi.framework.iexceltoolkit.toolkit.dto.IExportConfig;
import org.vivi.framework.iexceltoolkit.web.DataGenerator;
import org.vivi.framework.iexceltoolkit.web.request.IDynamicExportReq;
import org.vivi.framework.iexceltoolkit.web.request.ITemplateExportReq;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
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


//        List<Map<String, Object>> projectMaps = new DataGenerator().readJsonFile("json/genProjects.json");
//        dto.setDataList(projectMaps);
//        ExcelInvoke.templateExport(response, dto);
    }

    @PostMapping("/template/export1")
    public void templateExport1(HttpServletResponse response, @RequestBody ITemplateExportReq dto) throws Exception {
        List<Map<String, Object>> productMaps = new DataGenerator().readJsonFile("json/genProducts.json");
        dto.setDataList(productMaps);
//        Map<String, Object> otherVal = new HashMap<>();
//        otherVal.put("product", productMaps);
//        dto.setOtherVal(otherVal);
//        List<Map<String, Object>> projectMaps = new DataGenerator().readJsonFile("json/genProjects.json");
//        dto.setDataList(projectMaps);

        ExcelInvoke.templateExport(response, dto);


        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        IExportConfig exportConfig = new IExportConfig();

        List<Map<String, Object>> projectMaps = new DataGenerator().readJsonFile("json/genProjects.json");
        dto.setDataList(projectMaps);
        ExcelInvoke.templateExport(response, dto);
    }

    @Operation(summary = "导出excel")
    @GetMapping("/export-excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 读取资源文件（这样打成jar模板文件也在里面，防止模板文件被修改导致功能不可用，文件是在src/main/resources/excelTemplates下的）
        ClassPathResource resource = new ClassPathResource("template"+ File.separator+"template_mutiple.xlsx");
        // 组织并填充模板数据
        ByteArrayOutputStream byteArrayOutputStream = compositeFill(resource.getInputStream());

        // 这里文件名不起作用是因为前端是写死的
        String fileName = "多列表" + System.currentTimeMillis() + ".xlsx";
        // 设置响应头，告诉浏览器这是一个下载的文件，这里文件名
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 将文件内容写入响应输出流，浏览器可以直接触发下载
        response.getOutputStream().write(byteArrayOutputStream.toByteArray());
        response.getOutputStream().flush();
    }

    private ByteArrayOutputStream compositeFill(InputStream templateInputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 使用EasyExcel的模板填充功能，在这里指定合并单元格，这里应该是easyExcel的bug，第一列无法合并，其他列都可以，所以第一列单独用原生poi进行合并
        try  {
            ExcelWriter excelWriter = EasyExcel.write(byteArrayOutputStream).withTemplate(templateInputStream)
                    // 这里的参数就是圈了个合并区域，在这个正方形内的单元格都会合并，要注意索引是从0开始的，并且区域内不能存在已经合并的单元格否则报错
                    //.registerWriteHandler(new OnceAbsoluteMergeStrategy(2, 14, 9, 9))
                    //.registerWriteHandler(new OnceAbsoluteMergeStrategy(15, 27, 9, 9))
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            List<Map<String, Object>> productMaps = new DataGenerator().readJsonFile("json/genProducts.json");
            List<Map<String, Object>> projectMaps = new DataGenerator().readJsonFile("json/genProjects.json");
            // 防止上面两个表格覆盖下面两个表格，每一行都采用新增一行的方式
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            // 使用模板填充，必须使用FillWrapper，这是官方要求，并且每行两个表格只能有一个表格设置增行，否则会存在一个表格有空行，这里是造的测试数据
            excelWriter.fill(new FillWrapper("product", productMaps), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("project", projectMaps), writeSheet);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            Workbook workbook = new XSSFWorkbook(byteArrayInputStream);
            // 将修改后的内容写入 ByteArrayOutputStream
            workbook.write(outputStream);
            // 刷新一下，确保数据完全写入
            outputStream.flush();
            return outputStream;
            // 设置表格外的填充数据，例如总计、日期等数据
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("date", "2024年11月15日");
//            map.put("allInStaockQty", 1);
//            map.put("allOutStockQty", 2);
//            map.put("allConvertStockQty", 3);
//            map.put("allStockQty", 4);
//            map.put("convertCenterStockQty", 5);
//            map.put("mixOutStockQty", 6);
//            map.put("endItem1", 7);
//            map.put("endItem2", 8);
//            map.put("endItem3", 9);
//            map.put("endItem4", 10);
//            map.put("endItem5", 11);
//            excelWriter.fill(map, writeSheet);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 合并单元格，由于easyExcel自带的OnceAbsoluteMergeStrategy合并策略bug，这里需要用poi合并一下
        //return mergeCells(byteArrayOutputStream);
        return null;
    }

    public ByteArrayOutputStream mergeCells(ByteArrayOutputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStream.toByteArray());
             Workbook workbook = new XSSFWorkbook(byteArrayInputStream)) {
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);

            // 合并第1列第3行到第15行，注意行索引从0开始
            CellRangeAddress range1 = new CellRangeAddress(2, 14, 0,0);
            sheet.addMergedRegion(range1);

            // 合并第1列第16行到第28行，注意行索引从0开始
            CellRangeAddress range2 = new CellRangeAddress(15, 27, 0, 0);
            sheet.addMergedRegion(range2);

            // 设置合并单元格的样式
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            // 将修改后的内容写入 ByteArrayOutputStream
            workbook.write(outputStream);
            // 刷新一下，确保数据完全写入
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream;
    }


}
