package org.vivi.framework.ireport.demo.excel.style;


import org.vivi.framework.ireport.demo.excel.style.align.DefaultExcelAlign;
import org.vivi.framework.ireport.demo.excel.style.border.DefaultExcelBorders;
import org.vivi.framework.ireport.demo.excel.style.border.ExcelBorderStyle;
import org.vivi.framework.ireport.demo.excel.style.config.ExcelCellStyleConfigurer;

public class BlackHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(0, 0, 0)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}