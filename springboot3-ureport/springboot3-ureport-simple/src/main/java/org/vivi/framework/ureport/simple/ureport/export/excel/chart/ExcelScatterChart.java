package org.vivi.framework.ureport.simple.ureport.export.excel.chart;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFNoFillProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.AxisTickMark;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFScatterChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xddf.usermodel.text.XDDFRunProperties;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.ureport.simple.ureport.export.word.chart.ChartOption;

public class ExcelScatterChart implements ExcelChart {

private int sourceIndex = 0;
	
	@Override
	public int refreshSheet(XSSFSheet sheet, ChartOption options) {
		// 标题行
		int rowIndex = 0;
		Row row = sheet.createRow(rowIndex);
		String[] indicators = options.getIndicators();
		for (int i = 1; i < indicators.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(indicators[i]);
		}
		rowIndex++;
		JSONArray dataset = options.getDataSet();
		JSONObject source = dataset.getJSONObject(sourceIndex);
		JSONArray data = source.getJSONArray("source");
		for (Object object : data) {
			row = sheet.createRow(rowIndex);
			JSONArray rowArray = (JSONArray) object;
			// 分类
			Cell cell = row.createCell(0);
			cell.setCellValue(rowArray.getString(0));
			for (int i = 0; i < indicators.length; i++) {
				cell = row.createCell(i + 1);
				cell.setCellValue(rowArray.getDoubleValue(i + 1));
			}
			rowIndex++;
		}
		return rowIndex;
	}
	
	@Override
	public void bulider(XSSFSheet sheet,XSSFChart chart, ChartOption options) {
		sourceIndex = 0;
		// 字体颜色
		XDDFSolidFillProperties fontColor = new XDDFSolidFillProperties(XDDFColor.from(new byte[] {(byte)96,(byte)98,(byte)102}));
		// 样式线
		XDDFLineProperties line = new XDDFLineProperties();
		line.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(new byte[] {(byte)228,(byte)231,(byte)237})));
		line.setWidth(0.5);
		
		XDDFLineProperties noLine = new XDDFLineProperties();
		noLine.setFillProperties(new XDDFNoFillProperties());
				
		// X轴
		XDDFValueAxis  xAxis = chart.createValueAxis(AxisPosition.BOTTOM);
		if(!options.isShowXAxis()) {
			xAxis.setVisible(false);// X轴不显示
		}
		xAxis.setMajorTickMark(AxisTickMark.NONE);// 轴刻度线
		// 标签样式
		XDDFRunProperties xTextProperties = xAxis.getOrAddTextProperties();
		xTextProperties.setFillProperties(fontColor);
		// 轴线样式
		XDDFShapeProperties xAxisLineProperties = xAxis.getOrAddShapeProperties();
		if(options.isShowXAxisLine()) {
			xAxisLineProperties.setLineProperties(line);
		} else {
			xAxisLineProperties.setLineProperties(noLine);
		}
		// 网格线
		if(options.isShowXGrid()) {
			XDDFShapeProperties xGridProperties = xAxis.getOrAddMajorGridProperties();
			xGridProperties.setLineProperties(line);
		}
				
		// Y轴
		XDDFValueAxis  yAxis = chart.createValueAxis(AxisPosition.LEFT);
		if(!options.isShowYAxis()) {
			yAxis.setVisible(false);// Y轴不显示
		}
		yAxis.setMajorTickMark(AxisTickMark.NONE);// 轴刻度线
		// 标签样式
		XDDFRunProperties yTextProperties = yAxis.getOrAddTextProperties();
		yTextProperties.setFillProperties(fontColor);
		// 轴线样式
		XDDFShapeProperties yAxisLineProperties = yAxis.getOrAddShapeProperties();
		if(options.isShowYAxisLine()) {
			yAxisLineProperties.setLineProperties(line);
		} else {
			yAxisLineProperties.setLineProperties(noLine);
		}
		// 网格线
		if(options.isShowYGrid()) {
			XDDFShapeProperties yGridProperties = yAxis.getOrAddMajorGridProperties();
			yGridProperties.setLineProperties(line);
		}
				
		XDDFScatterChartData scatter = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, xAxis, yAxis);
		
		String[] dimensions = options.getDimensions();
		if(dimensions.length > 1) {
			sourceIndex = 0;
			XSSFWorkbook workbook = sheet.getWorkbook();
			for (int i = 1; i < dimensions.length; i++) {
				String d = dimensions[i];
				sheet = workbook.createSheet(d);
				int rowIndex = refreshSheet(sheet, options);
				if(rowIndex > 1) {
					XDDFNumericalDataSource<Double> x = XDDFDataSourcesFactory.fromNumericCellRange(sheet,new CellRangeAddress(1, rowIndex - 1, 1, 1));
					XDDFNumericalDataSource<Double> y = XDDFDataSourcesFactory.fromNumericCellRange(sheet,new CellRangeAddress(1, rowIndex - 1, 2, 2));
					XDDFScatterChartData.Series series = (XDDFScatterChartData.Series) scatter.addSeries(x, y);
					XDDFLineProperties chartLine = new XDDFLineProperties();
					chartLine.setFillProperties(new XDDFNoFillProperties());
					series.setLineProperties(chartLine);
					
					series.setMarkerSize((short)8);
					series.setMarkerStyle(MarkerStyle.CIRCLE);
					// 设置标记颜色
					CTMarker marker = chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(sourceIndex).getMarker();
					setMarkerColor(marker, options.getColor(sourceIndex));
				}
				sourceIndex++;
			}
		} else {
			int rowIndex = refreshSheet(sheet, options);
			if(rowIndex > 1) {
				XDDFNumericalDataSource<Double> x = XDDFDataSourcesFactory.fromNumericCellRange(sheet,new CellRangeAddress(1, rowIndex - 1, 1, 1));
				XDDFNumericalDataSource<Double> y = XDDFDataSourcesFactory.fromNumericCellRange(sheet,new CellRangeAddress(1, rowIndex - 1, 2, 2));
				XDDFScatterChartData.Series series = (XDDFScatterChartData.Series) scatter.addSeries(x, y);
				XDDFLineProperties chartLine = new XDDFLineProperties();
				chartLine.setFillProperties(new XDDFNoFillProperties());
				series.setLineProperties(chartLine);
				
				series.setMarkerSize((short)8);
				series.setMarkerStyle(MarkerStyle.CIRCLE);
				// 设置标记颜色
				CTMarker marker = chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(0).getMarker();
				setMarkerColor(marker, options.getColor(0));
			}
		}
		chart.plot(scatter);
	}
	
	/**
	 * 	设置标记点颜色
	 */
	private void setMarkerColor(CTMarker marker, byte[] color) {
		CTShapeProperties shapeProperties = marker.addNewSpPr();
		// 边框颜色
		CTLineProperties borderProperties = shapeProperties.addNewLn();
		CTSolidColorFillProperties borderColor = borderProperties.addNewSolidFill();
		CTSRgbColor borderRgb = borderColor.addNewSrgbClr();
		borderRgb.addNewAlpha().setVal(80000);  // 透明度 0- 100000
		borderRgb.setVal(color);
		// 填充颜色
		CTSolidColorFillProperties fillProperties = shapeProperties.addNewSolidFill();
		CTSRgbColor fillRgb = fillProperties.addNewSrgbClr();
		fillRgb.addNewAlpha().setVal(80000);  // 透明度 0- 100000
		fillRgb.setVal(color);
	}
}
