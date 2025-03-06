package org.vivi.framework.easyexcel.simple.service.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.easyexcel.simple.model.dto.ScreenExcelDTO;
import org.vivi.framework.easyexcel.simple.model.dto.ScreenGatherDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 第二部分表头横向合并策略
 */
public class ScreenScoreValueHorizontalMergeStrategy extends AbstractMergeStrategy {
    /**
     * 分组，每几行合并一次
     */
    private List<Integer> rowFieldGroupCountList;

    /**
     * 分组，每几列合并一次
     */
    private List<Integer> colFieldGroupCountList;

    /**
     * 目标合并列索引
     */
    private Integer targetColIndex;

    /**
     * 需要合并的行索引
     */
    private Integer targetRowIndex;

    /**
     * 需要开始合并单元格的首列索引
     */
    private Integer firstColumnIndex;

    /**
     * 表格数据第一行索引
     */
    private static final Integer FIRST_ROW_INDEX = 5;

    /**
     * 额外的长度为2
     */
    private static final Integer ADDITIONAL_LENGTH = 2;

    public ScreenScoreValueHorizontalMergeStrategy() {
    }

    /**
     * @param screenGatherDTOList     为待合并目标行的值
     * @param screenScoreExcelDTOList 为待合并目标列的值
     * @param firstColumnIndex        需要开始合并单元格的首列索引
     */
    public ScreenScoreValueHorizontalMergeStrategy(List<ScreenGatherDTO> screenGatherDTOList, List<ScreenExcelDTO> screenScoreExcelDTOList, Integer firstColumnIndex) {
        this.rowFieldGroupCountList = getRowGroupCountList(screenGatherDTOList);
        this.colFieldGroupCountList = getColGroupCountList(screenScoreExcelDTOList);
        this.firstColumnIndex = firstColumnIndex;
        this.targetRowIndex = FIRST_ROW_INDEX;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if (cell.getColumnIndex() == this.firstColumnIndex) {
            int columnCount = this.firstColumnIndex;
            // 列合并
            for (int i = 0; i < colFieldGroupCountList.size(); i++) {
                Integer count = colFieldGroupCountList.get(i);
                if (count == 1) {
                    columnCount += count;
                    continue;
                }
                // 行合并
                int rowIndexCount = 0;
                for (int j = 0; j < rowFieldGroupCountList.size(); j++) {
                    if (j == 0) {
                        rowIndexCount += this.targetRowIndex + rowFieldGroupCountList.get(j);
                    } else {
                        rowIndexCount += rowFieldGroupCountList.get(j);
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowIndexCount - ADDITIONAL_LENGTH - 1, rowIndexCount - ADDITIONAL_LENGTH - 1, columnCount, columnCount + count - 1);
                    sheet.addMergedRegionUnsafe(cellRangeAddress);
                    cellRangeAddress = new CellRangeAddress(rowIndexCount - ADDITIONAL_LENGTH, rowIndexCount - ADDITIONAL_LENGTH, columnCount, columnCount + count - 1);
                    sheet.addMergedRegionUnsafe(cellRangeAddress);

                }
                columnCount += count;
            }
        }
    }

    /**
     * 该方法将目标列根据值是否相同连续可合并，存储可合并的列数
     * 防止每个产品行数不同、不一定是4行的情况，该方法计算出每个产品的行数
     *
     * @param exportDataList
     * @return
     */
    private List<Integer> getRowGroupCountList(List<ScreenGatherDTO> exportDataList) {
        if (CollectionUtils.isEmpty(exportDataList)) {
            return new ArrayList<>();
        }
        List<Integer> groupCountList = new ArrayList<>();
        int count = 1;
        for (int i = 1; i < exportDataList.size(); i++) {
            boolean equals = exportDataList.get(i).getPartMode().equals(exportDataList.get(i - 1).getPartMode());

            if (equals) {
                count++;
            } else {
                groupCountList.add(count);
                count = 1;
            }
        }
        // 处理完最后一条后
        groupCountList.add(count);
        return groupCountList;
    }

    /**
     * 该方法将目标列根据值是否相同连续可合并，存储可合并的列数
     *
     * @param exportDataList
     * @return
     */
    private List<Integer> getColGroupCountList(List<ScreenExcelDTO> exportDataList) {
        if (CollectionUtils.isEmpty(exportDataList)) {
            return new ArrayList<>();
        }
        List<Integer> groupCountList = new ArrayList<>();
        int count = 1;
        for (int i = 1; i < exportDataList.size(); i++) {
            boolean equals = exportDataList.get(i).getTestItemName().equals(exportDataList.get(i - 1).getTestItemName());

            if (equals) {
                count++;
            } else {
                groupCountList.add(count);
                count = 1;
            }
        }
        // 处理完最后一条后
        groupCountList.add(count);
        return groupCountList;
    }
}

