package org.vivi.framework.iexcelbatch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别类型:0未知1男2女
 */

@Getter
@AllArgsConstructor
public enum SexEnum {

    /**
     * 未知
     */
    UNKNOW(0, "未知"),
    /**
     * 男
     */
    MAN(1, "男"),
    /**
     * 女
     */
    WOMAN(2, "女");


    private final Integer code;

    private final String desc;

}
