package org.vivi.framework.report.bigdata.paging1.consumer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.vivi.framework.report.bigdata.paging1.domain.BaseType;
import org.vivi.framework.report.bigdata.paging1.domain.CommonCallBackData;

import java.util.List;

public class TypeConsumer<T extends BaseType>  {

    public void richTextConsumer(CommonCallBackData<T> callBackData) {
        if (Boolean.TRUE.equals(callBackData.getIsHead())) {
            setRequireField(callBackData, "*");
            return;
        }
        Integer relativeRowIndex = callBackData.getRelativeRowIndex();
        List<T> dataList = callBackData.getDataList();
        T dto = dataList.get(relativeRowIndex);
        if (Boolean.TRUE.equals(dto.isGetRich())) {
            setRequireField(callBackData, "富文本");
        }
    }

    private static<T> void setRequireField(CommonCallBackData<T> callBackData, String markWord) {

        if (!callBackData.getAnnotation().isRequire()) {
            return;
        }
        Workbook workbook = callBackData.getWorkbook();
        Cell cell = callBackData.getCell();
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        String big = markWord;
        String value = cell.getStringCellValue() + big;
        int index = value.lastIndexOf(big);
        //将*三个字设置为红色字体
        RichTextString richTextString = new XSSFRichTextString(value);
        richTextString.applyFont(index, index + 1, font);
        cell.setCellValue(richTextString);
    }




    public  void cellStyleTextConsumer(CommonCallBackData<T> callBackData) {
        Integer relativeRowIndex = callBackData.getRelativeRowIndex();
        List<T> dataList = callBackData.getDataList();
        if(CollectionUtils.isEmpty(dataList)){
            return;
        }
        T dto = dataList.get(relativeRowIndex);
        if (Boolean.TRUE.equals(dto)) {
            CellStyle cellStyle = callBackData.getCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
    }
}
