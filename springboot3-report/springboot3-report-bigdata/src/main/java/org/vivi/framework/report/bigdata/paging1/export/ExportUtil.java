package org.vivi.framework.report.bigdata.paging1.export;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.report.bigdata.paging1.domain.Page;
import org.vivi.framework.report.bigdata.paging1.funtion.ExportFunction;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ExportUtil<T> {


    /**
     * registerWriteHandler()：注册写入处理器，用于自动调整列宽。LongestMatchColumnWidthStyleStrategy 是 EasyExcel 内置的一个实现类，用于根据单元格内容的长度，自动调整列宽。
     * relativeHeadRowIndex()：相对表头行索引，表示表头所在的行索引。默认为0，即表头在第一行。如果表头不在第一行，可以通过该方法设置相对的行索引。
     * registerConverter()：注册转换器，用于将 Java 对象与 Excel 单元格之间进行转换。如果读写 Excel 文件时需要进行数据类型转换，可以通过该方法注册转换器
     * <p>
     * <p>
     * 一个sheet装100w数据
     */
    public static final Long PER_SHEET_ROW_COUNT = 1000000L;
    /**
     * 每次查询20w数据，每次写入20w数据
     */
    public static final Long PER_WRITE_ROW_COUNT = 200000L;

    public Class<T> clazz;

    /**
     * 查询数据sql语句
     */
    private ExportFunction<Page, List<T>> bf;

    public ExportUtil(Class<T> clazz) {
        this.clazz = clazz;
    }



    public String exportExcel(ExportFunction<Page, List<T>> bf) {
        OutputStream out = null;
        try {
            //记录总数:实际中需要根据查询条件进行统计即可

            out = new FileOutputStream("G:/dd.xlsx");
//            Integer totalCount = 1000005;
            Long totalCount = bf.getCount();
            //每一个Sheet存放100w条数据
            Long sheetDataRows = PER_SHEET_ROW_COUNT;
            //每次写入的数据量20w,每页查询20W
            long writeDataRows = PER_WRITE_ROW_COUNT;
            //计算需要的Sheet数量
            long sheetNum = totalCount % sheetDataRows == 0 ? (totalCount / sheetDataRows) : (totalCount / sheetDataRows + 1);
            //计算一般情况下每一个Sheet需要写入的次数(一般情况不包含最后一个sheet,因为最后一个sheet不确定会写入多少条数据)
            long oneSheetWriteCount = sheetDataRows / writeDataRows;
            //计算最后一个sheet需要写入的次数
            long lastSheetWriteCount = totalCount % sheetDataRows == 0 ? oneSheetWriteCount : (totalCount % sheetDataRows % writeDataRows == 0 ? (totalCount / sheetDataRows / writeDataRows) : (totalCount / sheetDataRows / writeDataRows + 1));


            //必须放到循环外，否则会刷新流
            ExcelWriter excelWriter = EasyExcel.write(out).build();
            //开始分批查询分次写入
            for (int i = 0; i < sheetNum; i++) {
                //创建Sheet
                WriteSheet sheet = new WriteSheet();
                sheet.setSheetName("测试Sheet1" + i);
                sheet.setSheetNo(i);
                //循环写入次数: j的自增条件是当不是最后一个Sheet的时候写入次数为正常的每个Sheet写入的次数,如果是最后一个就需要使用计算的次数lastSheetWriteCount
                for (int j = 0; j < (i != sheetNum - 1 ? oneSheetWriteCount : lastSheetWriteCount); j++) {

//                   int start = i * sheetDataRows + j * writeDataRows + 1;
                    Long start = i * sheetDataRows + j * writeDataRows;

                    Page page = new Page(start, PER_WRITE_ROW_COUNT);
//
                    List<T> list = bf.apply(page);
                    WriteSheet writeSheet = EasyExcel.writerSheet(i, "员工信息" + (i + 1)).head(clazz)
                            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                            .build();
                    //写数据
                    excelWriter.write(list, writeSheet);
                }
            }
            // 下载EXCEL
            excelWriter.finish();
            out.flush();
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new UtilException("导出Excel失败，请联系网站管理员！");
        } finally {
            IoUtil.close(out);
        }
        return "sss";
    }


    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @return 结果
     */
    public void exportExcel(HttpServletResponse response, ExportFunction<Page, List<T>> bf, String fileName,
                            String sheetName) {


        OutputStream out = null;
        try {
            //记录总数:实际中需要根据查询条件进行统计即可
            Long totalCount = bf.getCount();
            //每一个Sheet存放100w条数据
            Long sheetDataRows = PER_SHEET_ROW_COUNT;
            //每次写入的数据量20w,每页查询20W
            long writeDataRows = PER_WRITE_ROW_COUNT;
            //计算需要的Sheet数量
            long sheetNum = totalCount % sheetDataRows == 0 ? (totalCount / sheetDataRows) : (totalCount / sheetDataRows + 1);
            //计算一般情况下每一个Sheet需要写入的次数(一般情况不包含最后一个sheet,因为最后一个sheet不确定会写入多少条数据)
            long oneSheetWriteCount = sheetDataRows / writeDataRows;
            //计算最后一个sheet需要写入的次数
            long lastSheetWriteCount = totalCount % sheetDataRows == 0 ? oneSheetWriteCount : (totalCount % sheetDataRows % writeDataRows == 0 ? (totalCount / sheetDataRows / writeDataRows) : (totalCount / sheetDataRows / writeDataRows + 1));

            out = response.getOutputStream();
            //必须放到循环外，否则会刷新流
            ExcelWriter excelWriter = EasyExcel.write(out).build();
            for (int i = 0; i < sheetNum; i++) {
                //创建Sheet
                WriteSheet sheet = new WriteSheet();
                sheet.setSheetName(sheetName + i);
                sheet.setSheetNo(i);
                //循环写入次数: j的自增条件是当不是最后一个Sheet的时候写入次数为正常的每个Sheet写入的次数,如果是最后一个就需要使用计算的次数lastSheetWriteCount
                for (int j = 0; j < (i != sheetNum - 1 ? oneSheetWriteCount : lastSheetWriteCount); j++) {
                    Long start = i * sheetDataRows + j * writeDataRows;
                    Page page = new Page(start, PER_WRITE_ROW_COUNT);
                    List<T> list = bf.apply(page);
                    WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetName + (i + 1)).head(clazz)
                            .build();
                    //写数据
                    excelWriter.write(list, writeSheet);
                }
            }
            // 下载EXCEL
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止浏览器端导出excel文件名中文乱码 当然和easyexcel没有关系
            String fileName1 = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodingFilename(fileName1));
            // 下载EXCEL
            excelWriter.finish();
            out.flush();
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
        } finally {
            IoUtil.close(out);
        }
    }


    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String fileName = URLEncoder.encode("员工信息", "UTF-8").replaceAll("\\+", "%20");
        String sssss = encodingFilename("员工信息");
        System.out.println(sssss);
    }
}
