package org.vivi.framework.ureport.simple.ureport.export.word.chart;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xwpf.usermodel.XWPFChart;

import com.alibaba.fastjson.JSONArray;

public interface WordChart {
	
	default int refreshSheet(XSSFSheet sheet, ChartOption options){
		// 标题行
		int rowIndex = 0;
		Row row = sheet.createRow(rowIndex);
		String[] dimensions = options.getDimensions();
		for (int i = 1; i < dimensions.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(dimensions[i]);
		}
		rowIndex++;
		JSONArray dataset = options.getDataSet();
		for (Object object : dataset) {
			row = sheet.createRow(rowIndex);
			JSONArray rowArray = (JSONArray) object;
			for (int i = 0; i < dimensions.length; i++) {
				Cell cell = row.createCell(i);
				if (i == 0) {
					cell.setCellValue(rowArray.getString(i));
				} else {
					cell.setCellValue(rowArray.getDoubleValue(i));
				}
			}
			rowIndex++;
		}
		return rowIndex;
	}
	
	void bulider(XWPFChart chart, ChartOption options);

}
