package org.vivi.framework.report.bigdata.entity.model;

import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.math.RoundingMode;

@Data
public class HeaderInfo {
    /**表头字段的顺序**/
    private Integer order;
    /**
     * 表头字段(对象字段名)
     **/
    private String name;
    /**
     * 表头(excel表头)
     **/
    private String title;
    /**表头字段提示**/
    private String prompt;
    /**字段值:下拉框**/
    private String[] dropdown;
    /**单元格值类型**/
    private Class<?> type;
    /**
     * 设置单元格内容格式
     * 浮点数,默认小数位格式
     */
    private String format;
    /**
     * 对内容的正则控制
     **/
    private String regex;
    /**
     * 四舍五入的模型
     **/
    private RoundingMode mode;
    /**
     * 对其方式
     **/
    private HorizontalAlignment align;
}