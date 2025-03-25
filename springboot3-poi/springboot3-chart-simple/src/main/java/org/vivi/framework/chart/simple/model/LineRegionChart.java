package org.vivi.framework.chart.simple.model;

import java.util.List;

/**
 * 折线/柱状区域类-用来定义整个图表
 */
public class LineRegionChart {
    /**
     * 图表的名称
     */
    private String titleName;
    /**
     * 横坐标
     */
    List<String> categories;
    /**
     * 横坐标名称
     */
    private String xTitle;
    /**
     * 纵坐标名称
     */
    private String yTitle;
    /**
     * 每条线对应的表格位置
     */
    List<TableSeries> series;


    public LineRegionChart(String titleName, List<String> categories, List<TableSeries> series) {
        this.series = series;
        this.titleName = titleName;
        this.categories = categories;
    }

    public String getxTitle() {
        return xTitle;
    }

    public void setxTitle(String xTitle) {
        this.xTitle = xTitle;
    }

    public String getyTitle() {
        return yTitle;
    }

    public void setyTitle(String yTitle) {
        this.yTitle = yTitle;
    }

    public List<TableSeries> getSeries() {
        return series;
    }

    public void setSeries(List<TableSeries> series) {
        this.series = series;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}