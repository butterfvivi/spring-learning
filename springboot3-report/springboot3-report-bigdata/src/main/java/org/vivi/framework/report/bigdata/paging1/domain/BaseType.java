package org.vivi.framework.report.bigdata.paging1.domain;


public class BaseType {

    /**
     * 如果要是excel是否需要使用富文本
     *
     * @return 是否需要使用富文本
     */
    public Boolean isGetRich() {
        return false;
    }


    /**
     * 设置单元格格式
     *
     * @return 是设置单元格格式
     */
    public Boolean isGetCellStyle() {
        return false;
    }


}
