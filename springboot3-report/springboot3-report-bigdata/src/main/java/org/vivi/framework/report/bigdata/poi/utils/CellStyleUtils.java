package org.vivi.framework.report.bigdata.poi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.vivi.framework.report.bigdata.poi.model.HeaderInfo;
import org.vivi.framework.report.bigdata.poi.model.ValidRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能 :
 *
 * @author : Bruce(刘正航) 10:53 AM 2018/12/5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CellStyleUtils {

    public static CellStyle defaultHeaderStyle(Workbook workbook){
        return configForgroundStyle(workbook,IndexedColors.BLUE_GREY);
    }

    public static CellStyle defaultOddStyle(Workbook workbook){
        return configForgroundStyle(workbook,null);
    }

    public static CellStyle defaultEvenStyle(Workbook workbook){
        return configForgroundStyle(workbook,IndexedColors.GREY_25_PERCENT);
    }

    public static CellStyle defaultMergeTitleStyle(Workbook workbook){
        return configFontColorStyle(workbook,HorizontalAlignment.LEFT,IndexedColors.RED);
    }

    private static CellStyle configForgroundStyle(Workbook workbook,IndexedColors forgroundColor) {
        return configCellStyle(workbook,HorizontalAlignment.CENTER,forgroundColor,null);
    }

    private static CellStyle configFontColorStyle(Workbook workbook,HorizontalAlignment align,IndexedColors fontColor) {
        return configCellStyle(workbook,align,null,fontColor);
    }

    private static CellStyle configCellStyle(Workbook workbook,HorizontalAlignment align,IndexedColors forgroundColor,IndexedColors fontColor){
        CellStyle style = workbook.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(align);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if( null != forgroundColor){
            style.setFillForegroundColor(forgroundColor.getIndex());
        }else{
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
        style.setWrapText(true);
        Font font = workbook.createFont();
        if( null != fontColor ){
            font.setColor(fontColor.getIndex());
        }
        font.setFontHeightInPoints(ValidRules.FONT_SIZE);
        font.setFontName(ValidRules.FONT_NAME);
        style.setFont(font);
        return style;
    }

    /**设置单元格样式的部分属性**/
    public static void setCellStyle(Workbook workbook,CellStyle cellStyle ,IndexedColors colors, Short fontSize, String fontName) {
        cellStyle.setFillForegroundColor(colors.index);
        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        cellStyle.setFont(font);
    }

    /**
     * 创建列样式
     **/
    public static List<CellStyle> columnCellStyle(Workbook workbook, List<String> headerNames, Map<String, HeaderInfo> headerInfos, CellStyle cellStyle) {
        List<CellStyle> cellStyles = new ArrayList<>();
        for (int i = 0, length = headerNames.size(); i < length; i++) {
            String headerName = headerNames.get(i);
            HeaderInfo headerInfo = headerInfos.get(headerName);
            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(cellStyle);
            if( null != headerInfo.getAlign() ){
                newCellStyle.setAlignment(headerInfo.getAlign());
            }

            //设置单元格格式
            String format = headerInfo.getFormat();
            if (StringUtils.isNotBlank(format)) {
                DataFormat dataFormat = workbook.createDataFormat();
                newCellStyle.setDataFormat(dataFormat.getFormat(format));
            }
            cellStyles.add(newCellStyle);
        }
        return cellStyles;
    }

}
