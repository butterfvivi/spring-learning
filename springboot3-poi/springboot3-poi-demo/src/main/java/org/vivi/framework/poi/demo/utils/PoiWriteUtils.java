package org.vivi.framework.poi.demo.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PoiWriteUtils {

    /**
     *
     * 1. 文件名 自动添加后缀 .xls
     * 2. sheetName 分页命名
     * 3. rowBeginIndex 起始行索引 哪行开始存储 0-第一行
     * 4. excel 行标题名称
     * 5. excel 列存放内容
     * 6. 文件生成路径
     */

    /**
     * 03版本 xls 文件写
     * @param fileName      文件名
     * @param sheetName     分页命名
     * @param rowBeginIndex 起始行索引 0-第一行
     * @param row           行标题内容
     * @param col           列存放内容
     * @param path          文件生成路径
     * @return true&false
     * @throws IOException
     */
    public boolean writeExcelByPoiHSSF(
            String fileName, String sheetName,
            int rowBeginIndex, List<String> row,
            List<List<Object>> col,
            String path
    ) throws IOException {

        // 处理文件后缀名 即 路径
        fileName += ".xls";
        path += fileName;

        // 创建表格
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        // 起始行
        Row row1 = sheet.createRow(rowBeginIndex);
        int rowLen = row.size();
        for (int i = 0; i < rowLen; i++) {
            // 第一行 第几列 初始化
            row1.createCell(i).setCellValue(row.get(i));
        }

        // 文件内容
        // 内容记录 行数
        int colLen = col.size();
        for (int i = rowBeginIndex + 1; i < colLen + rowBeginIndex + 1; i++) {
            // 多少行内容
            Row temp = sheet.createRow(i);
            for (int j = 0; j < col.get(i - rowBeginIndex - 1).size(); j++) {
                // 每行内容写入文件
                temp.createCell(j).setCellValue(col.get(i - rowBeginIndex - 1).get(j).toString());
            }
        }

        // IO操作
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);// 写文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
            workbook.close();
        }
        return true;
    }


    /**
     * 07 版本 xlsx 文件写
     * @param fileName
     * @param sheetName
     * @param rowBeginIndex
     * @param row
     * @param col
     * @param path
     * @return
     * @throws IOException
     */
    public boolean writeExcelByPoiXHSSF(
            String fileName, String sheetName,
            int rowBeginIndex, List<String> row,
            List<List<Object>> col,
            String path
    ) throws IOException{
        // 处理文件后缀名 即 路径
        fileName += ".xlsx";
        path += fileName;

        // 创建表格
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        // 起始行
        Row row1 = sheet.createRow(rowBeginIndex);
        int rowLen = row.size();
        for (int i = 0; i < rowLen; i++) {
            // 第一行 第几列 初始化
            row1.createCell(i).setCellValue(row.get(i));
        }

        // 文件内容
        // 内容记录 行数
        int colLen = col.size();
        for (int i = rowBeginIndex + 1; i < colLen + rowBeginIndex + 1; i++) {
            // 多少行内容
            Row temp = sheet.createRow(i);
            for (int j = 0; j < col.get(i - rowBeginIndex - 1).size(); j++) {
                // 每行内容写入文件
                temp.createCell(j).setCellValue(col.get(i - rowBeginIndex - 1).get(j).toString());
            }
        }

        // IO操作
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);// 写文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
            workbook.close();
        }
        return true;

    }

    /**
     * 大数据写 快速写重构
     * @param fileName
     * @param sheetName
     * @param rowBeginIndex
     * @param row
     * @param col
     * @param path
     * @return
     */
    public boolean writeExcelByPoiSXSSFBigData(
            String fileName, String sheetName,
            int rowBeginIndex, List<String> row,
            List<List<Object>> col,
            String path
    ) throws IOException{
        // 处理文件后缀名 即 路径
        fileName += ".xlsx";
        path += fileName;

        // 创建表格
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        // 起始行
        Row row1 = sheet.createRow(rowBeginIndex);
        int rowLen = row.size();
        for (int i = 0; i < rowLen; i++) {
            // 第一行 第几列 初始化
            row1.createCell(i).setCellValue(row.get(i));
        }

        // 文件内容
        // 内容记录 行数
        int colLen = col.size();
        for (int i = rowBeginIndex + 1; i < colLen + rowBeginIndex + 1; i++) {
            // 多少行内容
            Row temp = sheet.createRow(i);
            for (int j = 0; j < col.get(i - rowBeginIndex - 1).size(); j++) {
                // 每行内容写入文件
                temp.createCell(j).setCellValue(col.get(i - rowBeginIndex - 1).get(j).toString());
            }
        }

        // IO操作
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);// 写文件
            //清空临时文件
            ((SXSSFWorkbook)workbook).dispose();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
            workbook.close();
        }
        return true;
    }
}
