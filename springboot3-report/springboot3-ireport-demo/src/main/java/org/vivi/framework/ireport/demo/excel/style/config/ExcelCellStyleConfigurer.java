package org.vivi.framework.ireport.demo.excel.style.config;

import org.apache.poi.ss.usermodel.CellStyle;
import org.vivi.framework.ireport.demo.excel.style.align.ExcelAlign;
import org.vivi.framework.ireport.demo.excel.style.align.NoExcelAlign;
import org.vivi.framework.ireport.demo.excel.style.border.ExcelBorders;
import org.vivi.framework.ireport.demo.excel.style.border.NoExcelBorders;
import org.vivi.framework.ireport.demo.excel.style.color.DefaultExcelColor;
import org.vivi.framework.ireport.demo.excel.style.color.ExcelColor;
import org.vivi.framework.ireport.demo.excel.style.color.NoExcelColor;

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
