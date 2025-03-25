package org.vivi.framework.chart.simple.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PieChart {

    /**
     * 饼图每块的名称
     */
    private List<String> titleList;

    /**
     * 饼图每块的数据 这里的类型根据自己的实际情况给
     */
    private List<Integer> dataList;

    /**
     * 饼图标题名称
     */
    private String titleName;
}

