package org.vivi.framework.excel.configure.base.annotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExportTitle {
    /**
     * 表头描述
     *
     * @return
     */
    String title() default "";
}