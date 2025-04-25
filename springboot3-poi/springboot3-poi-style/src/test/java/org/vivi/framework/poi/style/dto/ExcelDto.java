package org.vivi.framework.poi.style.dto;


import org.vivi.framework.poi.style.annotation.DefaultHeaderStyle;
import org.vivi.framework.poi.style.annotation.ExcelColumn;
import org.vivi.framework.poi.style.annotation.ExcelColumnStyle;
import org.vivi.framework.poi.style.styleSet.BlackHeaderStyle;
import org.vivi.framework.poi.style.styleSet.BlueHeaderStyle;

@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
public class ExcelDto {

    @ExcelColumn(headerName = "name")
    private String name;

    private String hideColumn;

    @ExcelColumn(headerName = "age",
            headerStyle = @ExcelColumnStyle(excelCellStyleClass = BlackHeaderStyle.class))
    private int age;

}
