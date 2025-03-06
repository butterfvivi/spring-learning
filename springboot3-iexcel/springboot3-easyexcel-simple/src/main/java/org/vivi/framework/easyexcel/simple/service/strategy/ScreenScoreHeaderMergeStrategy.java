package org.vivi.framework.easyexcel.simple.service.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.easyexcel.simple.model.dto.ScreenExcelDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cauli
 * @date 2022/12/1 15:51
 * @description 第二部分表头合并策略
 */
public class ScreenScoreHeaderMergeStrategy extends AbstractMergeStrategy {
    /**
     * 分组，每几列合并一次
     */
    private List<Integer> exportFieldGroupCountList;

    /**
     * 目标合并行index
     */
    private Integer targetRowIndex;

    /**
     * 需要开始合并单元格的首列index
     */
    private Integer firstColumnIndex;

    /**
     * 0:表头 1：分类 2：测试项
     */
    private Integer mergeMark;

    /**
     * 额外队尾数量
     */
    private static final Integer TAILS_LENGTH = 2;

    /**
     * 表头第零行索引
     */
    private static final Integer HEADER_ROW_ZERO = 0;

    /**
     * 表头第一行索引
     */
    private static final Integer HEADER_ROW_ONE = 1;

    /**
     * 表头第二行索引
     */
    private static final Integer HEADER_ROW_TWO = 2;

    public ScreenScoreHeaderMergeStrategy() {
    }

    /**
     * @param mergeMark        0:表头 1：分类 2：测试项
     * @param exportDataList   为待合并目标列的值
     * @param targetRowIndex   合并的目标行索引
     * @param firstColumnIndex 需要开始合并单元格的首列索引
     */
    public ScreenScoreHeaderMergeStrategy(Integer mergeMark, List<ScreenExcelDTO> exportDataList, Integer targetRowIndex, Integer firstColumnIndex) {
        this.mergeMark = mergeMark;
        this.exportFieldGroupCountList = getGroupCountList(exportDataList);
        this.targetRowIndex = targetRowIndex;
        this.firstColumnIndex = firstColumnIndex;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if (cell.getColumnIndex() == this.firstColumnIndex && cell.getRowIndex() == targetRowIndex - 1) {
            int columnCount = this.firstColumnIndex;
            for (Integer count : exportFieldGroupCountList) {
                // 合并单元格
                CellRangeAddress cellRangeAddress = null;
                if (mergeMark == 0) {
                    cellRangeAddress = new CellRangeAddress(targetRowIndex - 1, targetRowIndex - 1, columnCount - 1, columnCount + count - 1);
                } else if (mergeMark == 1) {
                    if (count == 1) {
                        cellRangeAddress = new CellRangeAddress(HEADER_ROW_ONE, HEADER_ROW_TWO, columnCount, columnCount);
                    } else {
                        cellRangeAddress = new CellRangeAddress(HEADER_ROW_ONE, HEADER_ROW_TWO, columnCount, columnCount + count - 1);
                    }
                }

                sheet.addMergedRegionUnsafe(cellRangeAddress);
                columnCount += count;
            }
        }
    }

    /**
     * 该方法将目标列根据值是否相同连续可合并，存储可合并的列数
     *
     * @param exportDataList
     * @return
     */
    private List<Integer> getGroupCountList(List<ScreenExcelDTO> exportDataList) {
        if (CollectionUtils.isEmpty(exportDataList)) {
            return new ArrayList<>();
        }
        List<Integer> groupCountList = new ArrayList<>();
        int count = 1;
        for (int i = 1; i < exportDataList.size(); i++) {
            boolean equals = false;
            if (0 == mergeMark) {
                equals = exportDataList.get(i).getModelName().equals(exportDataList.get(i - 1).getModelName());
            } else {
                equals = exportDataList.get(i).getTestItemName().equals(exportDataList.get(i - 1).getTestItemName());
            }
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

