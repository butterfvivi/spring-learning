package org.vivi.framework.iexceltoolkit.common.converter;

import java.lang.annotation.*;

/**
 * 枚举转换器
 * <p>
 * 实现将枚举数据的值，格式化成枚举字典的标签
 *  导入  columnCode  columnName    不为空  根据反射遍历枚举找到对应的code值  getEnumCode
 * 导出  columnCode  columnName    不为空   根据code反射遍历枚举找到对应的值  getEnumValue
 * <p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnumFormat {

    /**
     * 例如说，枚举类型
     *
     * @return 字典类型
     */
    Class<? extends Enum> value();

    /**
     * 要翻译的字段方法
     */
    String columnCode() default "";


    /**
     * 要翻译的字段方法
     */
    String columnName() default "";
}
