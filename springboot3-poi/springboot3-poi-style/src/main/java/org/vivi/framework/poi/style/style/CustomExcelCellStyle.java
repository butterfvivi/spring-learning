package org.vivi.framework.poi.style.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.vivi.framework.poi.style.style.configurer.ExcelCellStyleConfigurer;

public abstract class CustomExcelCellStyle implements ExcelCellStyle {

	private ExcelCellStyleConfigurer configurer = new ExcelCellStyleConfigurer();

	public CustomExcelCellStyle() {
		configure(configurer);
	}

	public abstract void configure(ExcelCellStyleConfigurer configurer);

	@Override
	public void apply(CellStyle cellStyle) {
		configurer.configure(cellStyle);
	}

}
