package org.vivi.framework.report.bigdata.poi.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Format {
    /**
     * 文本格式
     **/
    public static final String TEXT = "@";
    /**
     * 保留小数2位
     **/
    public static final String DECIMAL_2 = "0.00";
    /**
     * 保留小数3位
     **/
    public static final String DECIMAL_3 = "0.000";
    /**
     * 保留小数4位
     **/
    public static final String DECIMAL_4 = "0.0000";
    /**
     * 保留小数5位
     **/
    public static final String DECIMAL_5 = "0.00000";
}