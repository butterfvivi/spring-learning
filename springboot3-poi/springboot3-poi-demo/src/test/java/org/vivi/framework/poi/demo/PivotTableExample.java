package org.vivi.framework.poi.demo;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class PivotTableExample {
    public static void main(String[] args) throws IOException {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet dataSheet = workbook.createSheet("Data");

        // 创建数据
        Object[][] data = {
                {"Name", "Age", "City"},
                {"John", 30, "New York"},
                {"Anna", 25, "London"},
                {"Mike", 35, "Paris"},
                {"Emily", 28, "New York"}
        };

        // 写入数据到工作表
        for (int i = 0; i < data.length; i++) {
            Row row = dataSheet.createRow(i);
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = row.createCell(j);
                if (data[i][j] instanceof String) {
                    cell.setCellValue((String) data[i][j]);
                } else if (data[i][j] instanceof Integer) {
                    cell.setCellValue((Integer) data[i][j]);
                }
            }
        }

        // 创建数据透视表
        XSSFSheet pivotSheet = (XSSFSheet) workbook.createSheet("Pivot Table");
        AreaReference source = workbook.getCreationHelper().createAreaReference("Data!A1:C5");
        CellReference position = new CellReference("A1");
        XSSFPivotTable pivotTable = pivotSheet.createPivotTable(source, position);

        // 配置数据透视表字段
        pivotTable.addRowLabel(0); // 将 "Name" 作为行标签
        pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 1, "Sum of Age"); // 将 "Age" 汇总为总和
        pivotTable.addReportFilter(2); // 将 "City" 作为筛选器

        // 保存工作簿
        try (FileOutputStream fileOut = new FileOutputStream("PivotTableExample.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
