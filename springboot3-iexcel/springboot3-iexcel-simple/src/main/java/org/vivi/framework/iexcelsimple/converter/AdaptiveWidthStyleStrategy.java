package org.vivi.framework.iexcelsimple.converter;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自适应宽度策略
 * 根据每列内容宽度自适应调整，宽度最小不低于10，最大不超过50
 *
 * 由于 LongestMatchColumnWidthStyleStrategy  自适应的宽度3.v 版本在适用模板导出时数据没有的值情况会列挤在一起 适应自定义大小的适应宽度
 *
 *
 */
public  class AdaptiveWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {
    /**
     * 存储sheet索引：列索引：宽度
     */
    private final Map<Integer, Map<Integer, Integer>> cache = new HashMap<>(2);

    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer integer, Boolean isHead) {
        boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
        if (!needSetWidth) {
            return;
        }
        // 计算列宽
        Integer columnWidth = getColumnWidth(cellDataList, cell, isHead);
        if (columnWidth < 0) {
            return;
        }
        // 最大不超过50，最小不小于10
        columnWidth = Math.min(columnWidth, 50);
        columnWidth = Math.max(columnWidth, 10);

        Map<Integer, Integer> maxColumnWidthMap = cache.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>(16));
        Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
        if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
            maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
            Sheet sheet = writeSheetHolder.getSheet();
            sheet.setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        }

    }

    /**
     * 计算宽度
     *
     * @param cellDataList
     * @param cell
     * @param isHead
     * @return
     */
    @SuppressWarnings("all")
    private Integer getColumnWidth(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            // 列头宽度打个折，当这列数据的宽度都很小时，列头文字会换行以节省空间
            return stringWidth(cell.getStringCellValue()) * 8 / 10;
        }
        CellData cellData = cellDataList.get(0);
        CellDataTypeEnum type = cellData.getType();
        if (type == null) {
            return -1;
        }
        switch (type) {
            case STRING:
                return stringWidth(cellData.getStringValue());
            case BOOLEAN:
                return cellData.getBooleanValue().toString().getBytes().length;
            case NUMBER:
                return cellData.getNumberValue().toString().getBytes().length;
            default:
                return -1;
        }

    }

    /**
     * 字符串宽度（中文宽度x2）
     */
    private int stringWidth(String str) {
        int chineseLength = (str.getBytes().length - str.length()) / 2;
        int otherLength = str.length() - chineseLength;
        return chineseLength * 2 + otherLength + 1;
    }


}
