package org.vivi.framework.excel.dynamic1.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 标题设置
 */
public class CustomTitleWriteHandler implements SheetWriteHandler {
    /**
     * 标题
     */
    private final String fileName;

    /**
     * 字段个数
     */
    private final Integer count;

    public CustomTitleWriteHandler(Integer count,String fileName) {
        this.fileName = fileName;
        this.count = count;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 获取clazz所有的属性
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);
        Row row1 = sheet.createRow(0);
        row1.setHeight((short) 800);
        Cell cell = row1.createCell(0);
        //设置标题
        cell.setCellValue(fileName);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 400);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, count-1));
    }
}

