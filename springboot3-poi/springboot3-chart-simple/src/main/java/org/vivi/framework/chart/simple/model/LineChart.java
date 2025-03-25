package org.vivi.framework.chart.simple.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LineChart {

    /**
     * 图表的名称
     */
    private String chartTitle;

    /**
     * 每条折线的名称
     */
    private List<String> titleList;

    /**
     * 每条折线对应的数据 这里的类型根据自己的实际情况给
     */
    private List<List<Integer>> dataList;

    /**
     * x轴 这里的类型根据自己的实际情况给
     */
    private List<Object> xAxisList;
}

