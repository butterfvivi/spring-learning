package org.vivi.framework.report.bigdata.common.annotation;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.*;
import java.math.RoundingMode;

/**
 * Excel行注释-用于获取Excel表头
 **/
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Header {

    /**列索引:指定多个字段排列先后顺序,从小到大排序: 可以是字母,也可以是数字**/
    String order() default "0";
    /**
     * 表头字段
     **/
    String name() default "";
    /**
     * 字段提示
     **/
    String prompt() default "";

    String[] dropdown() default {};

    /**字段的写入类型**/
    Class<?> type() default Object.class;

    /**
     * 支持自定义,只要符合excel标准即可
     * 设置单元格内容格式(文本格式,小数精度格式,日期格式,等)
     * 浮点数,默认小数位格式
     * 备注:
     * 如果是使用到csv工具类中,此字段则用于字符串的格式化(日期格式化,等)
     */
    String format() default "";

    /**
     * 对内容的正则控制
     **/
    String regex() default "";

    /**
     * 四舍五入的模型
     **/
    RoundingMode mode() default RoundingMode.UNNECESSARY;

    /**
     * 对其方式
     **/
    HorizontalAlignment align() default HorizontalAlignment.CENTER;
}