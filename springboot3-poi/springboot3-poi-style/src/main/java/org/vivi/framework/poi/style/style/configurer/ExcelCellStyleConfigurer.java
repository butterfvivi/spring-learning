package org.vivi.framework.poi.style.style.configurer;


import org.apache.poi.ss.usermodel.CellStyle;
import org.vivi.framework.poi.style.style.align.ExcelAlign;
import org.vivi.framework.poi.style.style.align.NoExcelAlign;
import org.vivi.framework.poi.style.style.border.ExcelBorders;
import org.vivi.framework.poi.style.style.border.NoExcelBorders;
import org.vivi.framework.poi.style.style.color.DefaultExcelColor;
import org.vivi.framework.poi.style.style.color.ExcelColor;
import org.vivi.framework.poi.style.style.color.NoExcelColor;

public class ExcelCellStyleConfigurer {

	private ExcelAlign excelAlign = new NoExcelAlign();
	private ExcelColor foregroundColor = new NoExcelColor();
	private ExcelBorders excelBorders = new NoExcelBorders();

	public ExcelCellStyleConfigurer() {

	}

	public ExcelCellStyleConfigurer excelAlign(ExcelAlign excelAlign) {
		this.excelAlign = excelAlign;
		return this;
	}

	public ExcelCellStyleConfigurer foregroundColor(int red, int blue, int green) {
		this.foregroundColor = DefaultExcelColor.rgb(red, blue, green);
		return this;
	}

	public ExcelCellStyleConfigurer excelBorders(ExcelBorders excelBorders) {
		this.excelBorders = excelBorders;
		return this;
	}

	public void configure(CellStyle cellStyle) {
		excelAlign.apply(cellStyle);
		foregroundColor.applyForeground(cellStyle);
		excelBorders.apply(cellStyle);
	}

}
