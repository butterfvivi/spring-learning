package org.vivi.framework.excel.dynamic1.handler.style;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * 单元各简单设置
 */
public class CellStyle extends AbstractColumnWidthStyleStrategy {

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head,
                                  Integer relativeRowIndex, Boolean isHead) {
        // 简单设置
        Sheet sheet = writeSheetHolder.getSheet();
        sheet.setColumnWidth(cell.getColumnIndex(), 5000);
    }

}

