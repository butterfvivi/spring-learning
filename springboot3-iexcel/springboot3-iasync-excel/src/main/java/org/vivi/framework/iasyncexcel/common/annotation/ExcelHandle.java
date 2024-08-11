package org.vivi.framework.iasyncexcel.common.annotation;

import java.lang.annotation.*;

/**
 * 注解在开发自定义的导入导出逻辑中，会通过扫描作为bean被注册到子容器中
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelHandle {
    String name() default "";
}
