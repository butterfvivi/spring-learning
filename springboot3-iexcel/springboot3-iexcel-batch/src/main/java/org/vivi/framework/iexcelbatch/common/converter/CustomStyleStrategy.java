package org.vivi.framework.iexcelbatch.common.converter;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

public class CustomStyleStrategy extends AbstractCellStyleStrategy {

    private static final short HEADER_COLOR_INDEX = IndexedColors.LIGHT_BLUE.getIndex();

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        CellStyle cellStyle = cell.getCellStyle();

        cellStyle.setFillForegroundColor(HEADER_COLOR_INDEX);
        //cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Workbook workbook = cell.getSheet().getWorkbook();
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);;
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }
}
