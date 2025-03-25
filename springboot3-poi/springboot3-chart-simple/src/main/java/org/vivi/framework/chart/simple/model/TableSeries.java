package org.vivi.framework.chart.simple.model;

import org.apache.poi.ss.util.CellRangeAddress;
/**
 * 表格线条类-用来定义折线图每条线对应的表格区域
 */
public class TableSeries {
    /**
     * 单元格范围
     */
    private CellRangeAddress cellRangeAddress;
    /**
     * 线的名称
     */
    private String name;

    public CellRangeAddress getCellRangeAddress() {
        return cellRangeAddress;
    }

    public void setCellRangeAddress(CellRangeAddress cellRangeAddress) {
        this.cellRangeAddress = cellRangeAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
