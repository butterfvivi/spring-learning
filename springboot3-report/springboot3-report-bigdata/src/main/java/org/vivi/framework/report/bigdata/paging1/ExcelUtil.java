package org.vivi.framework.report.bigdata.paging1;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
public class ExcelUtil {


    public static void writeExcel(HttpServletResponse response, List list, String fileName,
                                  String sheetName, Class clazz) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String reFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + reFileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            EasyExcel.write(response.getOutputStream(), clazz)
                    .inMemory(true)
                    .sheet(sheetName).doWrite(list);
        } catch (IOException e) {
            log.error("write excel fail,io exception", e);
        }
    }

    /**
     * 导出excel
     *
     * @param response          输出流
     * @param dataList          导出的数据
     * @param clazz             模板类
     * @param sheetName         sheetName
     * @param cellWriteHandlers 样式处理类
     */
    public static void writeExcelWithRegisterWriteHandler(HttpServletResponse response, List dataList, String fileName,
                                                          String sheetName, Class clazz, SheetWriteHandler sheetWriteHandlers, CellWriteHandler... cellWriteHandlers) {
        try {

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String reFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + reFileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            writeWithTitleAndSpinnerHandler(response.getOutputStream(), sheetName, clazz, dataList, sheetWriteHandlers, cellWriteHandlers);
        } catch (IOException e) {
            log.error("write excel fail,io exception", e);
        }
    }

    public static void writeExcelWithRegisterWriteHandler(List dataList, String fileName,
                                                          String sheetName, Class clazz, SheetWriteHandler sheetWriteHandlers, CellWriteHandler... cellWriteHandlers) {
        String filePath = "G:/demo3.xlsx";
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            writeWithTitleAndSpinnerHandler(outputStream, sheetName, clazz, dataList, sheetWriteHandlers, cellWriteHandlers);
            outputStream.close();
        } catch (IOException e) {
            log.error("write excel fail, io exception", e);
        }
    }

    private static void writeWithTitleAndSpinnerHandler(OutputStream outputStream, String sheetName, Class clazz, List dataList,
                                                        SheetWriteHandler sheetWriteHandlers,
                                                        CellWriteHandler[] cellWriteHandlers) {

        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(outputStream, clazz)
                .inMemory(true)
                .registerWriteHandler(sheetWriteHandlers)
                .sheet(sheetName);
        if (null != cellWriteHandlers && cellWriteHandlers.length > 0) {
            for (int i = 0; i < cellWriteHandlers.length; i++) {
                excelWriterSheetBuilder.registerWriteHandler(cellWriteHandlers[i]);
            }
        }

        excelWriterSheetBuilder.doWrite(dataList);
    }

}

