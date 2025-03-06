package org.vivi.framework.easyexcel.simple.service.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.easyexcel.simple.model.dto.ScreenGatherDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yicheng1.he
 * @date 2022/12/1 14:06
 * @description 第一部分表格数据合并策略
 */
public class ScreenValueMergeStrategy extends AbstractMergeStrategy {
    /**
     * 分组，每几行合并一次
     */
    private List<Integer> exportFieldGroupCountList;

    /**
     * 合并的目标开始列索引
     */
    private Integer targetBeginColIndex;
    /**
     * 合并的目标结束列索引
     */
    private Integer targetEndColIndex;
    /**
     * 需要开始合并单元格的首行索引
     */
    private Integer firstRowIndex;

    public ScreenValueMergeStrategy() {
    }

    /**
     * @param exportDataList      待合并目标行的值
     * @param targetBeginColIndex 合并的目标开始列索引
     * @param targetEndColIndex   合并的目标结束列索引
     * @param firstRowIndex       需要开始合并单元格的首行索引
     */
    public ScreenValueMergeStrategy(List<ScreenGatherDTO> exportDataList, Integer targetBeginColIndex, Integer targetEndColIndex, Integer firstRowIndex) {
        this.exportFieldGroupCountList = getGroupCountList(exportDataList);
        this.targetBeginColIndex = targetBeginColIndex;
        this.targetEndColIndex = targetEndColIndex;
        this.firstRowIndex = firstRowIndex;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if (cell.getRowIndex() == this.firstRowIndex && cell.getColumnIndex() >= targetBeginColIndex - 1 && cell.getColumnIndex() <= targetEndColIndex - 1) {
            int rowCount = this.firstRowIndex;
            for (Integer count : exportFieldGroupCountList) {
                if (count == 1) {
                    rowCount += count;
                    continue;
                }
                // 合并单元格
                CellRangeAddress cellRangeAddress;
                for (int i = 0; i < targetEndColIndex - targetBeginColIndex + 1; i++) {
                    cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount + count - 2, i, i);
                    sheet.addMergedRegionUnsafe(cellRangeAddress);
                }
                rowCount += count;
            }
        }
    }

    /**
     * 该方法将目标列根据值是否相同连续可合并，存储可合并的行数
     *
     * @param exportDataList
     * @return
     */
    private List<Integer> getGroupCountList(List<ScreenGatherDTO> exportDataList) {
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
}

