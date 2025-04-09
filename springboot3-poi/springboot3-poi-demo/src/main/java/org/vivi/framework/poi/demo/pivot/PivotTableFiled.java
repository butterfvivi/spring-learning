package org.vivi.framework.poi.demo.pivot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PivotTableFiled {
    String header();
    /* 标记该属性在透视表中的位置  */
    PivotTableArea area() default PivotTableArea.NOT_REQUIRED;
    /* 透视表的别名 */
    String aliasName() default "";
    /* 值汇总方式 */
    SummaryType summaryType() default SummaryType.COUNT;

}
