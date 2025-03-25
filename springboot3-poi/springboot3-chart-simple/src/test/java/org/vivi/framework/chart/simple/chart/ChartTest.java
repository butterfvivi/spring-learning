package org.vivi.framework.chart.simple.chart;


import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vivi.framework.chart.simple.model.ChartPosition;
import org.vivi.framework.chart.simple.model.LineChart;
import org.vivi.framework.chart.simple.model.PieChart;
import org.vivi.framework.chart.simple.utils.ChartUtils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChartTest {

    // 开始行
    public static int chartRowStart = 3;
    // 结束行
    public static int chartRowEnd = 20;

    public static ChartPosition chartPosition;

    public static void main(String[] args) throws IOException {
        // 填充数据
        XSSFWorkbook workbook = createExcel();
        FileOutputStream fileOut = null;
        try {
            // 将输出写入excel文件
            String filename = UUID.randomUUID() + ".xlsx";
            fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            //DesktopHelpers.openFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    public static XSSFWorkbook createExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        //-------------------------折线图--------------------------
        List<LineChart> lineCharts = initLineChart();
        for (LineChart lineChart : lineCharts) {
            // 图表位置（左上角坐标，右下角坐标） 左上角坐标的（列，行），（右下角坐标）列，行,偏移量均为0
            chartPosition = new ChartPosition()
                    .setRow1(chartRowStart)
                    .setCol1(0)
                    .setRow2(chartRowEnd)
                    .setCol2(lineChart.getXAxisList().size() + 3);
            ChartUtils.createLine(sheet, chartPosition, lineChart);
        }
        chartRowStart = chartRowEnd + 2;
        chartRowEnd = chartRowStart + 20;

        //-------------------------柱状图--------------------------
        XSSFRow row = sheet.createRow(chartRowStart);
        row.setHeight((short) 500);
        XSSFCell cell0 = row.createCell(0);
        cell0.setCellValue("测试柱状图");
        cell0.setCellStyle(ChartUtils.tableNameCellStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(chartRowStart, chartRowStart, 0, 2));
        // 获取数据
        List<PieChart> pieCharts = initPieChart();
        for (PieChart pieChart : pieCharts) {
            // 图表位置（左上角坐标，右下角坐标） 左上角坐标的（列，行），（右下角坐标）列，行,偏移量均为0
            chartPosition = new ChartPosition()
                    .setRow1(chartRowStart + 1)
                    .setCol1(0)
                    .setRow2(chartRowEnd)
                    .setCol2(8);
            ChartUtils.createBar(sheet, chartPosition, pieChart);
        }
        chartRowStart = chartRowEnd + 2;
        chartRowEnd = chartRowStart + 15;

        //-------------------------饼图--------------------------
        XSSFRow row1 = sheet.createRow(chartRowStart);
        row1.setHeight((short) 500);
        XSSFCell cell1 = row1.createCell(0);
        cell1.setCellValue("测试饼图");
        cell1.setCellStyle(ChartUtils.tableNameCellStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(chartRowStart, chartRowStart, 0, 2));
        // 获取数据
        for (PieChart pieChart : pieCharts) {
            // 图表位置（左上角坐标，右下角坐标） 左上角坐标的（列，行），（右下角坐标）列，行,偏移量均为0
            chartPosition = new ChartPosition()
                    .setRow1(chartRowStart + 1)
                    .setCol1(0)
                    .setRow2(chartRowEnd)
                    .setCol2(8);
            ChartUtils.createPie(sheet, chartPosition, pieChart);
        }
        chartRowStart = chartRowEnd + 2;
        chartRowEnd = chartRowStart + 15;

        //-------------------------Excel--------------------------
        // 获取列表数据
        ChartUtils.createTable(chartRowStart, workbook, sheet);

        // 去除网格线
        // sheet.setDisplayGridlines(false);
        return workbook;
    }

    /**
     * 折线图
     * @return
     */
    public static List<LineChart> initLineChart() {
        List<LineChart> lineCharts = new ArrayList<>();
        lineCharts.add(new LineChart()
                .setChartTitle("折线图")
                .setTitleList(Arrays.asList("2020年", "2021年"))
                .setDataList(Arrays.asList(Arrays.asList(1, 2, 3, 3, 1, 6, 3, 7, 12, 11, null, null), Arrays.asList(5, 4, 0, null, 12, 3, 8, 9, 11, 9, 2, 1)))
                .setXAxisList(Arrays.<Object>asList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月")));
        return lineCharts;
    }

    /**
     * 饼图
     * @return
     */
    public static List<PieChart> initPieChart() {
        List<PieChart> pieCharts = new ArrayList<>();
        String[] name = {"北京", "上海", "广东", "深圳"};
        String[] title = {"城市占比"};
        Integer[] data1 = {15, 3, 5, 9};
        for (int i = 0; i < title.length; i++) {
            PieChart pieChart = new PieChart();
            pieChart.setTitleList(Arrays.asList(name));
            pieChart.setTitleName(title[i]);
            pieChart.setDataList(Arrays.asList(data1));
            pieCharts.add(pieChart);
        }
        return pieCharts;
    }
}
