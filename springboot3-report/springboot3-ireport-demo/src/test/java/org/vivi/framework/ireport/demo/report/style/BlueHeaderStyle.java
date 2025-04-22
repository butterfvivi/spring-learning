package org.vivi.framework.ireport.demo.report.style;


import org.vivi.framework.ireport.demo.report.style.align.DefaultExcelAlign;
import org.vivi.framework.ireport.demo.report.style.border.DefaultExcelBorders;
import org.vivi.framework.ireport.demo.report.style.border.ExcelBorderStyle;
import org.vivi.framework.ireport.demo.report.style.config.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}