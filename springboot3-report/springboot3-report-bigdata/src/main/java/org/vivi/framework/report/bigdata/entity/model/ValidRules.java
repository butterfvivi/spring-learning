package org.vivi.framework.report.bigdata.entity.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidRules {

    /**
     * Excel首个有效行和Excel表头之间的行数差
     **/
    public static final int MAX_HEADER_ROW_NUM = 20;

    /**
     * 保留小数的正则
     **/
    public static final String REGEX_NUMBER = "^0(\\.[0])*$";

    /**
     * 中文左括号
     **/
    public static final String CHINA_LEFT_BRACKET = "（";

    /**
     * 回车换行
     **/
    public static final String NEW_LINE_SIGN = "\r\n";

    /**
     * 字体大小设置
     **/
    public static final Short FONT_SIZE = 11;

    /**
     * 字体类型
     **/
    public static final String FONT_NAME = "微软雅黑";

    /**默认单元格宽度**/
    public static final int DEFAULT_CELL_WIDTH = 20;

    /**
     * 默认列宽的倍数256是最大值(256*255)
     **/
    public static final int DEFAULT_CELL_WIDTH_MULTIPLE = 180;

    /**
     * 默认列宽的倍数256是最大值(256*255)
     **/
    public static final int DEFAULT_CELL_WIDTH_WIDTH = 255;
}
