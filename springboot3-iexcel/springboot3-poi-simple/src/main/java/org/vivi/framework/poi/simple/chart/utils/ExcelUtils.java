package org.vivi.framework.poi.simple.chart.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;

public class ExcelUtils {
    /**
     * 创建表头样式
     * @param wb
     * @return
     */
    public static XSSFCellStyle createHeadCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());//背景颜色
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        XSSFFont headerFont = wb.createFont(); // 创建字体样式
        headerFont.setBold(true); //字体加粗
        headerFont.setFontName("宋体"); // 设置字体类型
        headerFont.setFontHeightInPoints((short) 12); // 设置字体大小
        cellStyle.setFont(headerFont); // 为标题样式设置字体样式

        return cellStyle;
    }
    /**
     * 创建标题样式
     * @param wb
     * @return
     */
    public static XSSFCellStyle createTitleCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());//背景颜色

        XSSFFont headerFont1 = wb.createFont(); // 创建字体样式
        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontName("宋体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 16); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式

        return cellStyle;
    }
    /**
     * 创建内容样式
     * @param wb
     * @return
     */
    public static XSSFCellStyle createRemarkCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);// 两边对齐
        cellStyle.setAlignment(HorizontalAlignment.LEFT);// 居左
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        // 生成12号字体
        XSSFFont font = wb.createFont();
        font.setColor((short)8);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        return cellStyle;
    }
    /**
     * 创建内容样式
     * @param wb
     * @return
     */
    public static XSSFCellStyle createContentCellStyle(XSSFWorkbook wb) {
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
        font.setColor((short)8);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        return cellStyle;
    }
    /**
     * 创建内容样式
     * @param wb
     * @return
     */
    public static XSSFCellStyle createContentNumericalCellStyle(XSSFWorkbook wb,String format) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        XSSFDataFormat dataFormat = wb.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat(format));
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        // 生成12号字体
        XSSFFont font = wb.createFont();
        font.setColor((short)8);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 折线图
     *
     * @param sheet sheet
     * @param chartTitle 折线图标题
     * @param lineName 折线名称
     * @param xName x轴名称
     * @param yName y轴名称
     * @param firstRow 开始行
     * @param dataEndRow 结束行
     * @param startCol 开始列
     * @param xAxisCol x轴列
     * @param valueCol y轴列
     */
    public static void createSheetLineChark(XSSFSheet sheet, String chartTitle,String lineName,String xName,String yName, int firstRow, int dataEndRow, int startCol, int xAxisCol, int valueCol) {
        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前四个默认0，[0,4]：从0列4行开始;[7,20]:宽度7个单元格，20向下扩展到20行
        //默认宽度(14-8)*12
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, startCol, dataEndRow + 4, startCol + 9, dataEndRow + 28);
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题
        chart.setTitleText(chartTitle);
        //标题是否覆盖图表
        chart.setTitleOverlay(false);
        //图例位置
//        XDDFChartLegend legend = chart.getOrAddLegend();
//        legend.setPosition(LegendPosition.TOP);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        if(StringUtils.isNotBlank(xName))
            bottomAxis.setTitle(xName);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        if(StringUtils.isNotBlank(yName))
            leftAxis.setTitle(yName);
        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //分类轴标(X轴)数据，单元格范围位置[0, 0]到[0, 6]
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, dataEndRow, xAxisCol, xAxisCol));

        //数据1，单元格范围位置[1, 0]到[1, 6]
        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, dataEndRow, valueCol, valueCol));

        //LINE：折线图，
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        //图表加载数据，折线1
        XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(xData, values);
        //折线图例标题
        if(StringUtils.isNotBlank(lineName))
            series1.setTitle(lineName, null);
        //直线
        series1.setSmooth(false);
        //设置标记大小
        series1.setMarkerSize((short) 6);

        //设置标记样式 无
        series1.setMarkerStyle(MarkerStyle.CIRCLE);
//        series1.setShowLeaderLines(false);

        //绘制
        chart.plot(data);
    }

    /**
     * 饼图
     *
     * @param sheet sheet
     * @param chartTitle 标题
     * @param firstRow  开始行
     * @param dataEndRow 结束行
     * @param startCol 开始列
     * @param xAxisCol x轴列
     * @param valueCol 结束列
     */
    public static void createSheetPieChark(XSSFSheet sheet,String chartTitle,int firstRow,int dataEndRow,int startCol,int xAxisCol,int valueCol){
        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前四个默认0，[0,4]：从0列4行开始;[7,20]:宽度7个单元格，20向下扩展到20行
        //默认宽度(14-8)*12
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, startCol, dataEndRow + 4, startCol + 20, dataEndRow + 40);
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题
        chart.setTitleText(chartTitle);
        //标题是否覆盖图表
        chart.setTitleOverlay(false);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);
        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //分类轴标数据，
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, dataEndRow, xAxisCol, xAxisCol));
        //数据1，单元格范围位置[1, 0]到[1, 6]
        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, dataEndRow, valueCol, valueCol));
        //XDDFChartData data = chart.createData(ChartTypes.PIE3D, null, null);
        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        //设置为可变颜色
        data.setVaryColors(true);
        //图表加载数据
        data.addSeries(xData, values);

        //绘制
        chart.plot(data);

    }

    /**
     * 柱状图
     *
     * @param sheet
     * @param chartTitle
     * @param firstRow
     * @param dataEndRow
     * @param startCol
     * @param xAxisCol
     * @param valueCol
     */
    public static void createSheetBarChark(XSSFSheet sheet,String chartTitle,int firstRow,int dataEndRow,int startCol,int xAxisCol,int valueCol){
        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前四个默认0，[0,4]：从0列4行开始;[7,20]:宽度7个单元格，20向下扩展到20行
        //默认宽度(14-8)*12
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, startCol, dataEndRow + 4, startCol + 20, dataEndRow + 45);
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题
        chart.setTitleText(chartTitle);
        //标题是否覆盖图表
        chart.setTitleOverlay(false);

        //图例位置
//        XDDFChartLegend legend = chart.getOrAddLegend();
//        legend.setPosition(LegendPosition.TOP);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //分类轴标数据，
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, dataEndRow, xAxisCol, xAxisCol));

        //数据1，单元格范围位置[1, 0]到[1, 6]
        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, dataEndRow, valueCol, valueCol));
        //bar：条形图，
        XDDFBarChartData bar = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);

        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        //设置为可变颜色
        bar.setVaryColors(true);
        //条形图方向，纵向/横向：纵向
        bar.setBarDirection(BarDirection.COL);

        //图表加载数据，条形图1
        XDDFBarChartData.Series series1 = (XDDFBarChartData.Series) bar.addSeries(xData, values);
        //条形图例标题

        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.BLUE));
        //条形图，填充颜色
        series1.setFillProperties(fill);
        //绘制
        chart.plot(bar);
    }

    /**
     * 创建画布
     *
     * @param sheet
     * @param chartTitle
     * @param dataEndRow
     * @param startCol
     * @return
     */
    public static XSSFChart createSheetDrawing(XSSFSheet sheet, String chartTitle, int dataEndRow, int startCol) {
        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前四个默认0，[0,4]：从0列4行开始;[7,20]:宽度7个单元格，20向下扩展到20行
        //默认宽度(14-8)*12
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, startCol, dataEndRow + 4, startCol + 9, dataEndRow + 28);
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题
        chart.setTitleText(chartTitle);
        //标题是否覆盖图表
        chart.setTitleOverlay(false);
        return chart;
    }


    /**
     * 创建折线图
     *
     * @param sheet
     * @param yDataList y轴数据
     * @param title
     * @param firstRow
     * @param endRow
     * @param startCol
     * @param endCol
     */
    public static void createLineChart(XSSFSheet sheet, List<Double> yDataList, String title, int firstRow, int endRow, int startCol, int endCol, int firstRowOffset, int endRowOffset, int startColOffset, int endColOffset) {

        //创建一个画布
        XSSFChart chart = getSheetDrawing(sheet, title, startCol, firstRowOffset, endRowOffset, startColOffset, endColOffset);

        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        //LINE：折线图，
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

        // X轴数据
        XDDFCategoryDataSource countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, endRow, startCol, endCol));
        Double[] doubles = new Double[yDataList.size()];
        for (int i= 0; i< yDataList.size(); i++) {
            doubles[i] = yDataList.get(i);
        }
        // Y轴数据
        XDDFNumericalDataSource<Double> source = XDDFDataSourcesFactory.fromArray(doubles);

        XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(countries, source);
        series.setTitle(title, null);
        //直线
        series.setSmooth(false);
        //设置标记大小
        series.setMarkerSize((short) 6);

        //绘制
        chart.plot(data);
    }

    /**
     * 饼图
     *
     * @param sheet sheet
     * @param chartTitle 标题
     * @param firstRow  开始行
     * @param endRow 结束行
     * @param startCol 开始列
     * @param endCol 结束列
     * @param firstRowOffset  饼图开始y坐标偏移量
     * @param endRowOffset    饼图结束y坐标偏移量
     * @param startColOffset  饼图开始x坐标偏移量
     * @param endColOffset    饼图结束x坐标偏移量
     */
    public static void createPieChart(XSSFSheet sheet, List<Double> yDataList, String chartTitle, int firstRow, int endRow, int startCol, int endCol, int firstRowOffset, int endRowOffset, int startColOffset, int endColOffset) {
        //创建一个画布
        XSSFChart chart = getSheetDrawing(sheet, chartTitle, startCol, firstRowOffset, endRowOffset, startColOffset, endColOffset);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);
        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //分类轴标数据，
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, endRow, startCol, endCol));

        Double[] doubles = new Double[yDataList.size()];
        for (int i= 0; i< yDataList.size(); i++) {
            doubles[i] = yDataList.get(i);
        }
        // Y轴数据
        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromArray(doubles);
        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        //设置为可变颜色
        data.setVaryColors(true);
        //图表加载数据
        data.addSeries(xData, values);

        //绘制
        chart.plot(data);

    }

    /**
     * 柱状图
     *
     * @param sheet
     * @param yDataList
     * @param chartTitle
     * @param firstRow
     * @param endRow
     * @param startCol
     * @param endCol
     * @param firstRowOffset
     * @param endRowOffset
     * @param startColOffset
     * @param endColOffset
     */
    public static void createBarChart(XSSFSheet sheet, List<Double> yDataList, String chartTitle, int firstRow, int endRow, int startCol, int endCol, int firstRowOffset, int endRowOffset, int startColOffset, int endColOffset) {
        // 获取画布
        XSSFChart chart = getSheetDrawing(sheet, chartTitle, startCol, firstRowOffset, endRowOffset, startColOffset, endColOffset);
        //获取或添加图例
        XDDFChartLegend legend = chart.getOrAddLegend();
        //设置图例的位置
        legend.setPosition(LegendPosition.BOTTOM);
        //创建 类别轴 放置底部
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        //创建数据轴 放置左侧
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//        leftAxis.setTitle(chartTitle);
        //创建数据中心  0，0
        XDDFCategoryDataSource xs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, endRow, startCol, endCol));

        Double[] doubles = new Double[yDataList.size()];
        for (int i= 0; i< yDataList.size(); i++) {
            doubles[i] = yDataList.get(i);
        }
        XDDFNumericalDataSource<Double> xv = XDDFDataSourcesFactory.fromArray(doubles);
        //数据加载
        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFChartData.Series series = data.addSeries(xs, xv);
        series.setTitle(chartTitle, null);
        XDDFBarChartData bar = (XDDFBarChartData) data;
        bar.setVaryColors(true);
        bar.setBarDirection(BarDirection.COL);
        bar.setBarGrouping(BarGrouping.STACKED);
        chart.plot(data);
    }

    /**
     * 创建画布
     *
     * @param sheet
     * @param chartTitle
     * @param startCol
     * @param firstRowOffset
     * @param endRowOffset
     * @param startColOffset
     * @param endColOffset
     * @return
     */
    private static XSSFChart getSheetDrawing(XSSFSheet sheet, String chartTitle, int startCol, int firstRowOffset, int endRowOffset, int startColOffset, int endColOffset) {
        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前四个默认0，[0,4]：从0列4行开始;[7,20]:宽度7个单元格，20向下扩展到20行

        //默认宽度(14-8)*12
        int lastRowNum = sheet.getLastRowNum();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, startCol + startColOffset, lastRowNum + firstRowOffset, startCol + endColOffset, lastRowNum + endRowOffset);
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题
        chart.setTitleText(chartTitle);
        //标题是否覆盖图表
        chart.setTitleOverlay(false);
        return chart;
    }

}

