package org.vivi.framework.poi.style.resource.collection;


import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.vivi.framework.poi.style.resource.DataFormatDecider;
import org.vivi.framework.poi.style.resource.ExcelCellKey;
import org.vivi.framework.poi.style.style.ExcelCellStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * PreCalculatedCellStyleMap
 *
 * Determines cell's style
 * In currently, PreCalculatedCellStyleMap determines {org.apache.poi.ss.usermodel.DataFormat}
 *
 */
public class PreCalculatedCellStyleMap {

	private final DataFormatDecider dataFormatDecider;

	public PreCalculatedCellStyleMap(DataFormatDecider dataFormatDecider) {
		this.dataFormatDecider = dataFormatDecider;
	}

	private final Map<ExcelCellKey, CellStyle> cellStyleMap = new HashMap<>();

	public void put(Class<?> fieldType, ExcelCellKey excelCellKey, ExcelCellStyle excelCellStyle, Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		DataFormat dataFormat = wb.createDataFormat();
		cellStyle.setDataFormat(dataFormatDecider.getDataFormat(dataFormat, fieldType));
		excelCellStyle.apply(cellStyle);
		cellStyleMap.put(excelCellKey, cellStyle);
	}

	public CellStyle get(ExcelCellKey excelCellKey) {
		return cellStyleMap.get(excelCellKey);
	}

	public boolean isEmpty() {
		return cellStyleMap.isEmpty();
	}

}
