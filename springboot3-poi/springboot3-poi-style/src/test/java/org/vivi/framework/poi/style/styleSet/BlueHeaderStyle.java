package org.vivi.framework.poi.style.styleSet;


import org.vivi.framework.poi.style.style.CustomExcelCellStyle;
import org.vivi.framework.poi.style.style.align.DefaultExcelAlign;
import org.vivi.framework.poi.style.style.border.DefaultExcelBorders;
import org.vivi.framework.poi.style.style.border.ExcelBorderStyle;
import org.vivi.framework.poi.style.style.configurer.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}