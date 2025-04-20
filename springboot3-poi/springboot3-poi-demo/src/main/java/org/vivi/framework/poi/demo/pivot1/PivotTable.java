package org.vivi.framework.poi.demo.pivot1;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PivotTable {

    private final String DATASOURCE_SHEET_NAME = "sheet1";
    private final String EXPORT_EXCEL_NAME = "导出-交易数据分析.xlsx";
    @Getter
    @Setter
    private XSSFWorkbook workbook;
    @Getter
    @Setter
    private XSSFSheet sheet1;
    @Getter
    @Setter
    private XSSFSheet pivotSheet;

    /**
     * 数据源所在sheet表名称
     */
    private String dataSourceSheetName;
    ///**
    // * 生成的透视表sheet名称
    // */
    //@Getter
    //@Setter
    //private String pivoSheetName = "";

    /**
     * 要处理的Excel路径
     */
    @Getter
    @Setter
    private String excelFilePath = "";

    public PivotTable(String excelFilePath, String dataSourceSheetName) throws IOException {
        this.excelFilePath = excelFilePath;
        this.dataSourceSheetName = dataSourceSheetName;

        loadExcelFile();
    }

    private void loadExcelFile() throws IOException {
        //创建一个模板文件，里面自己手动填入数据
        FileInputStream file = new FileInputStream(new File(excelFilePath));
        this.workbook = new XSSFWorkbook(file);
        this.sheet1 = workbook.getSheet(dataSourceSheetName);
        //System.out.println(sheet1.getRow(0).getPhysicalNumberOfCells());
    }


    public void createSheeet(String pivoSheetName) {
        // 创建数据透视sheet
        pivotSheet = workbook.createSheet(pivoSheetName);
        pivotSheet.setDefaultColumnWidth(50 * 256);

    }


    public XSSFPivotTable createPivotTable() {
        // 获取数据sheet的总行数
        int num = this.sheet1.getLastRowNum() + 1;
        // 数据透视表数据源的起点单元格位置
        CellReference topLeft = new CellReference("A1");
        // 数据透视表数据源的终点单元格位置
        String cellRef = excelColIndexToStr(sheet1.getRow(0).getPhysicalNumberOfCells()) + num;
        CellReference botRight = new CellReference(cellRef );

        // 数据透视表生产的起点单元格位置
        CellReference ptStartCell = new CellReference("A1");
        AreaReference areaR = new AreaReference(topLeft, botRight, SpreadsheetVersion.EXCEL2007);
        XSSFPivotTable pivotTable = pivotSheet.createPivotTable(areaR, ptStartCell, sheet1);

        return pivotTable;
    }


    public String saveExcel() throws IOException {
        File file=new File(EXPORT_EXCEL_NAME);
        FileOutputStream outputFile = new FileOutputStream(file);
        this.workbook.write(outputFile);
        outputFile.close();
        return file.getAbsolutePath();
    }

    /**
     * Excel列索引转字母值
     *
     * @param columnIndex 列索引（1开始）
     * @return
     */
    private String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }
}
