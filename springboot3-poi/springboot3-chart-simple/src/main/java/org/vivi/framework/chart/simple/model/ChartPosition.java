package org.vivi.framework.chart.simple.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ChartPosition {
    /**
     * 图表的左上角坐标列
     */
    private int col1;
    /**
     * 图表的左上角坐标行
     */
    private int row1;
    /**
     * 图表的右下角坐标列
     */
    private int col2;
    /**
     * 图表的右下角坐标行t
     */
    private int row2;

    /**
     * 下面的为偏移量均设置为0
     */
    private int dx1 = 0;

    private int dy1 = 0;

    private int dx2 = 0;

    private int dy2 = 0;

}

