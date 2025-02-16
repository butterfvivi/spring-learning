package org.vivi.framework.excel.dynamic1.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.excel.common.exception.CommonException;
import org.vivi.framework.excel.common.utils.DateUtil;
import org.vivi.framework.excel.dynamic1.handler.CustomTitleWriteHandler;
import org.vivi.framework.excel.dynamic1.handler.entity.ExcelHeader;
import org.vivi.framework.excel.dynamic1.handler.style.CellStyle;


@Component
@Slf4j
public class ExcelUtil {

    /**
     * 从 sheet 中获取指定行列的单元格值
     *
     * @param sheet        表单
     * @param rowNumber    行号
     * @param columnNumber 列号
     * @return 统一返回字符串类型，null 表示找不到
     */
    public static String getCellValue(Sheet sheet, int rowNumber, int columnNumber) {

        Row row = sheet.getRow(rowNumber);
        if (null == row) {
            return null;
        }
        Cell cell = row.getCell(columnNumber);
        if (null == cell) {
            return null;
        }

        // 只处理数字、文本、公式结果为数字、文本的，其余认为错误
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                // 保留两位小数，并处理科学计数法
                DecimalFormat df = new DecimalFormat("0.00");
                return df.format(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                switch (cell.getCachedFormulaResultTypeEnum()) {
                    case STRING:
                        return cell.getStringCellValue();
                    case NUMERIC:
                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setGroupingUsed(false);
                        return String.valueOf(nf.format(cell.getNumericCellValue()));
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * 根据模板导出数据 单个sheet
     *
     * @param response     返回对象
     * @param dataList     导出的数据集合
     * @param object       填充对象
     * @param fileName     文件名称
     * @param templateName 模板名称
     * @throws Exception
     */
    public void exportTemplateExcel(HttpServletResponse response, List<?> dataList, Object object,
                                    String fileName, String templateName) throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream(templateName);
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        ExcelWriter excelWriter = EasyExcelFactory.write(getOutputStream(fileName, response)).withTemplate(inputStream).build();
        WriteSheet writeSheet0 = EasyExcelFactory.writerSheet(0).build();
        excelWriter.fill(object, fillConfig, writeSheet0);
        excelWriter.fill(dataList, fillConfig, writeSheet0);
        excelWriter.finish();
    }

    /**
     * 构建输出流
     *
     * @param fileName 文件名称
     * @param response 输出流
     * @return
     * @throws Exception
     */
    private OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        return response.getOutputStream();
    }

    /**
     * 文件格式校验
     *
     * @param file 文件
     */
    public  void checkFile(MultipartFile file) throws CommonException {
        if (file == null) {
            throw new CommonException("500", "文件不能为空");
        }
        String filename = file.getOriginalFilename();
        if (StrUtil.isEmpty(filename)) {
            throw new CommonException("500", "文件不能为空");
        }
        if (!filename.endsWith(".xls") && !filename.endsWith(".xlsx")) {
            throw new CommonException("500", "请上传.xls文件或者.xlsx文件");
        }
    }

    /**
     * 动态表头生成excel
     * @param headers 要生成的表头
     * @param dataList 数据
     * @param response
     * @param fileName 文件名称
     * @param titleName title名称
     * @param <T>
     */
    public static <T> void dynamicExportExcel(List<ExcelHeader> headers, List<T> dataList, HttpServletResponse response, String fileName, String titleName) {
        long startTime = System.currentTimeMillis();
        List<List<T>> allList = new ArrayList<>();
        for (T detail : dataList) {
            allList.addAll(dataList(headers, detail));
        }
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            String name = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "inline; " + name + ".xlsx");//设置响应头
            //response.setHeader("mime","application/vnd.ms-excel");
            EasyExcel.write(outputStream).head(headers(headers))
                    //表格标题占位
                    .relativeHeadRowIndex(1)
                    //文件样式
                    .registerWriteHandler(new CustomTitleWriteHandler(headers.size(),titleName))
                    .registerWriteHandler(new CellStyle())
                    .sheet(fileName).doWrite(allList);
        } catch (IOException e) {
            log.error("生成动态EXL失败，字段", e);
        }
        long endTime = System.currentTimeMillis();
        log.info("动态导出耗时:{}", endTime - startTime);
    }

    //excel表头
    public static List<List<String>> headers(List<ExcelHeader> excelHeaders) {
        List<List<String>> headers = new ArrayList<>();
        for (ExcelHeader header : excelHeaders) {
            List<String> head = new ArrayList<>();
            head.add(header.getHeadName());
            headers.add(head);
        }
        return headers;
    }

    /**
     * 要导出的字段
     *
     * @param exportFields 表头集合
     * @param obj          数据对象
     * @return 集合
     */
    @SneakyThrows
    public static <T> List<List<T>> dataList(List<ExcelHeader> exportFields, T obj) {
        List<List<T>> list = new ArrayList<>();
        List<T> data = new ArrayList<>();
        List<String> propList = exportFields.stream().map(ExcelHeader::getFieldName).collect(Collectors.toList());
        //先根据反射获取实体类的class对象
        Class<?> objClass = obj.getClass();
        //设置实体类属性的集合
        Field[] fields = ReflectUtil.getFields(objClass);
        for (String prop : propList) {
            //循环实体类对象集合
            for (Field field : fields) {
                field.setAccessible(true);
                //判断实体类属性跟特定字段集合名是否一样
                if (field.getName().equals(prop)) {
                    T object = (T) field.get(obj);
                    //获取属性对应的值
                    if(null == object){
                        object = (T) "--";
                    }else{
                        if(object instanceof LocalDate){
                            object = (T) DateUtil.localDateToString((LocalDate)object);
                        }
                        if(object instanceof LocalDateTime){
                            object = (T) DateUtil.localDateTimeToString((LocalDateTime)object);
                        }
                    }
                    data.add(object);
                }
            }
        }
        list.add(data);
        return list;
    }

}


