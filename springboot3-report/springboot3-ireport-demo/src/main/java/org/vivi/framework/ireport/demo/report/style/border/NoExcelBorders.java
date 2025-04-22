package org.vivi.framework.ireport.demo.report.style.border;

import org.apache.poi.ss.usermodel.CellStyle;

public final class NoExcelBorders implements ExcelBorders {

	@Override
	public void apply(CellStyle cellStyle) {
		// Do nothing
	}

}
