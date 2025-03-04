package org.vivi.framework.report.simple.utils;

import org.apache.commons.lang3.StringUtils;
import org.vivi.framework.report.simple.common.exception.BusinessExceptionBuilder;

import java.util.Collection;

public abstract class AssertUtils {

    /**
     * 目标对象判断非空，如果为空，抛出异常
     * @param value 判断的目标对象
     * @param code 异常码
     * @param args
     */
    public static void notNull(Object value,String code,Object... args) {
        if (value == null) {
            throw BusinessExceptionBuilder.build(code,args);
        }
    }

    /**
     * 目标字符串判断非空或非空字符串，是则抛出异常
     * @param value 判断的目标对象
     * @param code 异常码
     * @param args
     */
    public static void notEmpty(String value,String code,Object... args) {
        if (StringUtils.isBlank(value)) {
            throw BusinessExceptionBuilder.build(code,args);
        }
    }

    /**
     * 目标集合是否为空，是则抛出异常
     * @param value 判断的目标对象
     * @param code 异常码
     * @param args
     */
    public static void notEmpty(Collection value, String code, Object... args) {
        if (value == null || value.isEmpty()) {
            throw BusinessExceptionBuilder.build(code,args);
        }
    }


    /**
     * 目标表达式是否为true，否则抛出异常
     * @param value 判断的目标对象
     * @param code 异常码
     * @param args
     */
    public static void isTrue(boolean value, String code, Object... args) {
        if (value) {
            throw BusinessExceptionBuilder.build(code,args);
        }
    }

    /**
     * 目标表达式是否为false，否则抛出异常
     * @param value 判断的目标对象
     * @param code 异常码
     * @param args
     */
    public static void isFalse(boolean value, String code, Object... args) {
        if (!value) {
            throw BusinessExceptionBuilder.build(code,args);
        }
    }
}
