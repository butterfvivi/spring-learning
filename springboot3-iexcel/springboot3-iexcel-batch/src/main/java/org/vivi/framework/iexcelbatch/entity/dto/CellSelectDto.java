package org.vivi.framework.iexcelbatch.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CellSelectDto {

    /**
     * 起始行
     */
    private Integer firstRow;
    /**
     * 结束行
     */
    private Integer lastRow;
    /**
     * 起始列
     */
    private Integer firstCol;
    /**
     * 结束列
     */
    private Integer lastCol;

    /**
     * @param firstRow 行 从0开始
     * @param lastRow  设置下拉值的行数
     * @param firstCol 列 从0开始
     */
    public CellSelectDto(Integer firstRow, Integer lastRow, Integer firstCol) {
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
    }

    /**
     * 默认的行
     *
     * @param firstRow
     * @param firstCol
     */
    public CellSelectDto(Integer firstRow, Integer firstCol) {
        this.firstRow = firstRow;
        this.firstCol = firstCol;
    }
}
