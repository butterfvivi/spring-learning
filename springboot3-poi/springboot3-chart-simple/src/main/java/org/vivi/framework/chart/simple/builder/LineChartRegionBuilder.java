package org.vivi.framework.chart.simple.builder;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.vivi.framework.chart.simple.model.ChartPosition;
import org.vivi.framework.chart.simple.model.LineRegionChart;
import org.vivi.framework.chart.simple.model.TableSeries;


import java.util.Arrays;
import java.util.List;

public class LineChartRegionBuilder {
    /**
     * 表格类型
     */
    ChartTypes chartTypes;
    /**
     * 折线图对应表格位置设置
     */
    private LineRegionChart lineChartRegion;

    public LineChartRegionBuilder(LineRegionChart lineChartRegion) {
        this.lineChartRegion = lineChartRegion;
    }

    public LineChartRegionBuilder(ChartTypes chartTypes, LineRegionChart lineChartRegion) {
        this.chartTypes = chartTypes;
        this.lineChartRegion = lineChartRegion;
    }

    /**
     * 生成画布
     * @param sheet
     * @param chartPosition 在图表上的初始位置
     * @return
     */
    private XSSFChart createDrawingPatriarch(XSSFSheet sheet, ChartPosition chartPosition) {
        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前偏移量默认0
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, chartPosition.getCol1(), chartPosition.getRow1(), chartPosition.getCol2(), chartPosition.getRow2());
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题是否覆盖图表
        chart.setTitleOverlay(false);
        return chart;
    }

    /**
     * 生成图表
     * @param sheet
     * @param chartPosition 初始位置
     */
    public void createChart(XSSFSheet sheet, ChartPosition chartPosition) {
        //生成画布
        XSSFChart chart = createDrawingPatriarch(sheet, chartPosition);
        if ((chartTypes == null)) {
            chartTypes = ChartTypes.LINE;
        }
        switch (chartTypes){
            case BAR:
                createBarChart(sheet,chart);
                break;
            case LINE:
                createLineChart(sheet,chart);
                break;
            default:
        }
    }

    /**
     * 生成柱状图
     * @param sheet
     * @param chart 画布
     */
    public void createBarChart(XSSFSheet sheet, XSSFChart chart) {
        List<TableSeries> series = lineChartRegion.getSeries();
        String xTitle = lineChartRegion.getxTitle();
        String yTitle = lineChartRegion.getyTitle();
        List<String> categories = lineChartRegion.getCategories();
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.RIGHT);
        bottomAxis.setTitle(xTitle);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(yTitle);
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        //bar：条形图
        XDDFBarChartData bar = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        //设置为可变颜色
        bar.setVaryColors(true);
        //条形图方向，纵向/横向：纵向
        bar.setBarDirection(BarDirection.COL);
        //横坐标
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(categories.toArray(new String[]{}));
        for (int i = 0; i < series.size(); i++) {
            //分类轴标数据
            CellRangeAddress cellRangeAddress = series.get(i).getCellRangeAddress();
            XDDFNumericalDataSource<Double> dataSource = XDDFDataSourcesFactory.fromNumericCellRange(sheet,cellRangeAddress);
            XDDFBarChartData.Series addSeries = (XDDFBarChartData.Series) bar.addSeries(xData, dataSource);
            //图例标题
            addSeries.setTitle(series.get(i).getName(),null);
        }
        //绘制
        chart.plot(bar);
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        for (CTBarSer ser : plotArea.getBarChartList().get(0).getSerList()) {
            CTDLbls ctdLbls = ser.addNewDLbls();
            //引导线
            ctdLbls.addNewShowLeaderLines();
            // 是否展示数值
            ctdLbls.addNewShowVal().setVal(true);
            ctdLbls.addNewShowLegendKey().setVal(false);
            //类别名称
            ctdLbls.addNewShowCatName().setVal(false);
            ctdLbls.addNewShowSerName().setVal(false);
        }
    }

    /**
     * 生成折线图
     * @param sheet
     * @param chart 画布
     */
    private void createLineChart(XSSFSheet sheet,XSSFChart chart) {
        chart.setTitleText(lineChartRegion.getTitleName());
        String xTitle = lineChartRegion.getxTitle();
        String yTitle = lineChartRegion.getyTitle();
        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.RIGHT);
        bottomAxis.setTitle(xTitle);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(yTitle);
        //LINE：折线图
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        List<TableSeries> cellRangeAddressList = lineChartRegion.getSeries();
        List<String> categories = lineChartRegion.getCategories();
        //横坐标
        XDDFCategoryDataSource countries = XDDFDataSourcesFactory.fromArray(Arrays.copyOf(categories.toArray(), categories.toArray().length, String[].class));

        for (int i = 0; i < cellRangeAddressList.size(); i++) {
            TableSeries lineTableSeries = cellRangeAddressList.get(i);
            CellRangeAddress cellRangeAddress = lineTableSeries.getCellRangeAddress();
            String name = lineTableSeries.getName();
            XDDFNumericalDataSource<Double> dataSource = XDDFDataSourcesFactory.fromNumericCellRange(sheet,cellRangeAddress);
            //图表加载数据，折线
            XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(countries, dataSource);
            //条形图例标题
            XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.BLUE_VIOLET));
            //条形图，填充颜色
            series.setFillProperties(fill);
            //图例标题
            series.setTitle(name, null);
            //直线
            series.setSmooth(false);
            //设置标记大小
            series.setMarkerSize((short) 6);
            //设计标记样式
            series.setMarkerStyle(MarkerStyle.STAR);
            series.setFillProperties(fill);
        }
        //绘制
        chart.plot(data);
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        for (CTLineSer ser : plotArea.getLineChartList().get(0).getSerList()) {
            CTDLbls ctdLbls = ser.addNewDLbls();
            //引导线
            ctdLbls.addNewShowLeaderLines();
            // 是否展示数值
            ctdLbls.addNewShowVal().setVal(true);
            ctdLbls.addNewShowLegendKey().setVal(false);
            //类别名称
            ctdLbls.addNewShowCatName().setVal(false);
            ctdLbls.addNewShowSerName().setVal(false);
        }
    }
}