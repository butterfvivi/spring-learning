package org.vivi.framework.poi.demo.pivot1;

import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;

import java.io.File;
import java.io.IOException;

class PivotTableServiceTest {

    public static void main(String[] args) {

        try {
            //Scanner scanner = new Scanner(System.in);
            //System.out.println("请输入Excel路径：");
            //String excelPath = scanner.next();
            String excelPath = "/Users/vivi/IdeaProjects/spring-learning/springboot3-poi/springboot3-poi-demo/src/main/resources";
            if (excelPath.trim().length() == 0) return;

            File file = new File(excelPath);
            if (!file.exists()) {
                System.err.println("文件不存在，请检查输入的路径是否正确！！！");
                return;
            }

            System.out.println("处理中，请稍后...");
            PivotTable pivotTable = new PivotTable(excelPath, "sheet1");

            pivotTable.createSheeet("设备交易类型分析");
            XSSFPivotTable xssfPivotTable = pivotTable.createPivotTable();
            xssfPivotTable.addRowLabel(1);// 添加行
            xssfPivotTable.addRowLabel(3);// 添加行
            // 添加列
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 3, "交易笔数");
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 4, "交易额");
            //添加过滤 值是要过滤的条件列索引
            xssfPivotTable.addReportFilter(11);


            pivotTable.createSheeet("交易额统计");
            xssfPivotTable = pivotTable.createPivotTable();
            xssfPivotTable.addRowLabel(3);
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 4, "交易额");
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 3, "交易笔数");
            //添加过滤 值是要过滤的条件列索引
            xssfPivotTable.addReportFilter(11);


            pivotTable.createSheeet("季度交易额统计");
            xssfPivotTable = pivotTable.createPivotTable();
            xssfPivotTable.addRowLabel(13);
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 4, "交易额");
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 3, "交易笔数");
            //添加过滤 值是要过滤的条件列索引
            xssfPivotTable.addReportFilter(11);


            pivotTable.createSheeet("机构季度统计");
            xssfPivotTable = pivotTable.createPivotTable();
            xssfPivotTable.addRowLabel(2);
            xssfPivotTable.addColLabel(13);
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 4, "交易额");
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 3, "交易笔数");
            //添加过滤 值是要过滤的条件列索引
            xssfPivotTable.addReportFilter(11);

            pivotTable.createSheeet("设备季度统计");
            xssfPivotTable = pivotTable.createPivotTable();
            xssfPivotTable.addRowLabel(1);
            xssfPivotTable.addColLabel(13);
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 4, "交易额");
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 3, "交易笔数");
            //添加过滤 值是要过滤的条件列索引
            xssfPivotTable.addReportFilter(11);


            pivotTable.createSheeet("设备交易明细汇总");
            xssfPivotTable = pivotTable.createPivotTable();
            xssfPivotTable.addRowLabel(1);
            xssfPivotTable.addRowLabel(3);
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 3, "交易笔数");
            xssfPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 4, "交易额");
            //添加过滤 值是要过滤的条件列索引
            xssfPivotTable.addReportFilter(11);


            String exportFilePath = pivotTable.saveExcel();
            System.out.println("处理完成...,导出路径: " + exportFilePath);

        } catch (OLE2NotOfficeXmlFileException ex) {
            System.err.println("【异常提示】不支持的Excel文件，请尝试使用MicroSoft Excel打开并重新另存为Excel 2007及以上版本后尝试...");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}