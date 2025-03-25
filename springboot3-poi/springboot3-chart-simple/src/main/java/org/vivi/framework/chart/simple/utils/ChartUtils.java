package org.vivi.framework.chart.simple.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos;
import org.vivi.framework.chart.simple.model.ChartPosition;
import org.vivi.framework.chart.simple.model.LineChart;
import org.vivi.framework.chart.simple.model.PieChart;


import java.util.Arrays;
import java.util.List;

public class ChartUtils {
    private static XSSFChart createDrawingPatriarch(XSSFSheet sheet, ChartPosition chartPosition, String chartTitle) {
        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前偏移量四个默认0
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, chartPosition.getCol1(), chartPosition.getRow1(), chartPosition.getCol2(), chartPosition.getRow2());
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题
        chart.setTitleText(chartTitle);
        //标题是否覆盖图表
        chart.setTitleOverlay(false);
        return chart;
    }

    /**
     * 柱状图
     * @param sheet
     * @param chartPosition
     * @param pieChart
     */
    public static void createBar(XSSFSheet sheet, ChartPosition chartPosition, PieChart pieChart){
        String titleName = pieChart.getTitleName();
        List<String> titleList = pieChart.getTitleList();
        List<Integer> dataList = pieChart.getDataList();
        XSSFChart chart = createDrawingPatriarch(sheet, chartPosition, titleName);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        //分类轴标数据
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(titleList.toArray(new String[]{}));
        XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(dataList.toArray(new Integer[]{}));
        //bar：条形图
        XDDFBarChartData bar = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        //设置为可变颜色
        bar.setVaryColors(true);
        //条形图方向，纵向/横向：纵向
        bar.setBarDirection(BarDirection.COL);
        //图表加载数据，条形图1
        XDDFBarChartData.Series series1 = (XDDFBarChartData.Series) bar.addSeries(xData, values);
        //条形图例标题
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.BLUE_VIOLET));
        //条形图，填充颜色
        series1.setFillProperties(fill);
        //绘制
        chart.plot(bar);
    }

    /**
     * 创建饼图
     *
     * @param sheet 图表
     * @see com.gideon.entity.PieChart  饼图数据的封装
     * @see com.gideon.entity.ChartPosition 饼图的坐标位置
     */
    public static void createPie(XSSFSheet sheet, ChartPosition chartPosition, PieChart pieChart) {
        String titleName = pieChart.getTitleName();
        List<String> titleList = pieChart.getTitleList();
        List<Integer> dataList = pieChart.getDataList();
        XSSFChart chart = createDrawingPatriarch(sheet, chartPosition, titleName);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        //分类轴标数据
        XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromArray(titleList.toArray(new String[]{}));
        XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(dataList.toArray(new Integer[]{}));
        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        //设置为可变颜色
        data.setVaryColors(true);
        //图表加载数据
        data.addSeries(countries, values);
        //绘制
        chart.plot(data);
        CTDLbls ctdLbls = chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).addNewDLbls();
        ctdLbls.addNewShowVal().setVal(false);
        ctdLbls.addNewShowLegendKey().setVal(false);
        //类别名称
        ctdLbls.addNewShowCatName().setVal(false);
        //百分比
        ctdLbls.addNewShowSerName().setVal(false);
        ctdLbls.addNewShowPercent().setVal(true);
        //引导线
        ctdLbls.addNewShowLeaderLines().setVal(false);
        //分隔符为分行符
        ctdLbls.setSeparator("\n");
        //数据标签内
        ctdLbls.addNewDLblPos().setVal(STDLblPos.Enum.forString("inEnd"));
    }

    /**
     * 创建折线图
     *
     * @param sheet 图表
     * @see com.gideon.entity.PieChart  饼图数据的封装
     * @see com.gideon.entity.ChartPosition 饼图的坐标位置
     */
    public static void createLine(XSSFSheet sheet, ChartPosition chartPosition, LineChart lineChart) {
        List<Object> xAxisList = lineChart.getXAxisList();
        List<String> chartTitleList = lineChart.getTitleList();
        List<List<Integer>> chartDataList = lineChart.getDataList();
        String chartTitle = lineChart.getChartTitle();
        XSSFChart chart = createDrawingPatriarch(sheet, chartPosition, chartTitle);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        //LINE：折线图
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        XDDFCategoryDataSource countries = XDDFDataSourcesFactory.fromArray(Arrays.copyOf(xAxisList.toArray(), xAxisList.toArray().length, String[].class));
        for (int i = 0; i < chartDataList.size(); i++) {
            List<Integer> floats = chartDataList.get(i);
            XDDFNumericalDataSource<Integer> dataSource = XDDFDataSourcesFactory.fromArray(floats.toArray(new Integer[]{}));
            //图表加载数据，折线
            XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(countries, dataSource);
            series.setTitle(chartTitleList.get(i), null);
            //直线
            series.setSmooth(false);
            //设置标记大小
            series.setMarkerSize((short) 2);
            //添加标签数据，折线图中拐点值展示
            series.setShowLeaderLines(true);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls()
                    .addNewDLblPos().setVal(STDLblPos.CTR);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowVal().setVal(true);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowLegendKey().setVal(false);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowCatName().setVal(false);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowSerName().setVal(false);
        }
        //绘制
        chart.plot(data);
        if (chartDataList.size() == 1) {
            chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);
        }
    }

    /**
     * 创建列表
     */
    public static void createTable(int rowNum, XSSFWorkbook wb, XSSFSheet sheet) {
        // 样式
        XSSFCellStyle titleStyle = createTitleCellStyle(wb);
        XSSFCellStyle contentStyle = createContentCellStyle(wb);
        // 创建第一页的第一行，索引从0开始
        XSSFRow row = sheet.createRow(rowNum);
        row.setHeight((short) 500);
        XSSFCell cell0 = row.createCell(0);
        cell0.setCellValue("动态列表");
        cell0.setCellStyle(tableNameCellStyle(wb));
        // 合并单元格，参数依次为起始行，结束行，起始列，结束列 （索引0开始）
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));

        XSSFRow row1 = sheet.createRow(rowNum + 1);
        XSSFRow row2 = sheet.createRow(rowNum + 2);
        row1.setHeight((short) 600);
        row2.setHeight((short) 600);

        String title0 = "序号";
        XSSFCell cell = row1.createCell(0);
        cell.setCellValue(title0);
        // 合并单元格，参数依次为起始行，结束行，起始列，结束列 （索引0开始）
        CellRangeAddress region = new CellRangeAddress(rowNum + 1, rowNum + 2, 0, 0);
        sheet.addMergedRegion(region);
        // 合并之后为合并的单元格设置样式
        setRegionStyle(sheet, region, titleStyle);

        String title = "城市";
        XSSFCell c00 = row1.createCell(1);
        c00.setCellValue(title);
        // 合并单元格，参数依次为起始行，结束行，起始列，结束列 （索引0开始）
        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum + 1, rowNum + 2, 1, 1);
        sheet.addMergedRegion(cellRangeAddress);
        setRegionStyle(sheet, cellRangeAddress, titleStyle);

        String[] years = {"21年", "22年", "23年"};
        int startCellIndex = 2;
        int endCellIndex = 4;
        // 动态年份
        for (int i = 0; i < years.length; i++) {
            XSSFCell cell1 = row1.createCell(startCellIndex);
            cell1.setCellValue(years[i]);
            CellRangeAddress cellAddresses = new CellRangeAddress(rowNum + 1, rowNum + 1, startCellIndex, endCellIndex);
            sheet.addMergedRegion(cellAddresses);
            setRegionStyle(sheet, cellAddresses, titleStyle);
            XSSFCell cell11 = row2.createCell(startCellIndex++);
            cell11.setCellValue("动态列1");
            cell11.setCellStyle(titleStyle);
            XSSFCell cell2 = row2.createCell(startCellIndex++);
            cell2.setCellValue("动态列2");
            cell2.setCellStyle(titleStyle);
            XSSFCell cell3 = row2.createCell(startCellIndex++);
            cell3.setCellValue("动态列3");
            cell3.setCellStyle(titleStyle);
            endCellIndex += 3;
        }

        rowNum += 3;
        for (int j = 0; j < 10; j++) {
            int k = j + 1;
            XSSFRow tempRow = sheet.createRow(rowNum);
            rowNum++;
            // 序号
            XSSFCell cell11 = tempRow.createCell(0);
            cell11.setCellValue(k);
            cell11.setCellStyle(contentStyle);
            // 城市
            XSSFCell cell2 = tempRow.createCell(1);
            cell2.setCellValue("城市" + k);
            cell2.setCellStyle(contentStyle);
            int columnIndex = 2;
            int k1 = 1;
            for (int i = 0; i < years.length; i++) {
                XSSFCell cell3 = tempRow.createCell(columnIndex++);
                cell3.setCellValue("测试" + k1++);
                cell3.setCellStyle(contentStyle);
                XSSFCell cell4 = tempRow.createCell(columnIndex++);
                cell4.setCellValue("测试" + k1++);
                cell4.setCellStyle(contentStyle);
                XSSFCell cell5 = tempRow.createCell(columnIndex++);
                cell5.setCellValue("测试" + k1++);
                cell5.setCellStyle(contentStyle);
            }
        }
    }

    /**
     * 为合并的单元格设置样式（可根据需要自行调整）
     */
    @SuppressWarnings("deprecation")
    public static void setRegionStyle(XSSFSheet sheet, CellRangeAddress region, XSSFCellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) row = sheet.createRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                XSSFCell cell = row.getCell(j);
                if (null == cell) cell = row.createCell(j);
                cell.setCellStyle(cs);
            }
        }
    }

    /**
     * 列表名称样式
     *
     * @param wb
     * @return
     */
    public static XSSFCellStyle tableNameCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐

        XSSFFont headerFont1 = wb.createFont(); // 创建字体样式
        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 12); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式
        return cellStyle;
    }

    /**
     * 创建标题样式
     *
     * @param wb
     * @return
     */
    private static XSSFCellStyle createTitleCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//背景颜色
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        XSSFFont headerFont1 = wb.createFont(); // 创建字体样式
//        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 12); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式
        return cellStyle;
    }

    /**
     * 创建内容样式
     *
     * @param wb
     * @return
     */
    private static XSSFCellStyle createContentCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        // 生成12号字体
        XSSFFont font = wb.createFont();
        font.setColor((short) 8);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        return cellStyle;
    }

}

