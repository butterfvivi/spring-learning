package org.vivi.framework.iasyncexcel.starter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ExcelHandleBasePackagesRegistrar.class,ExcelImportSelector.class})
public @interface EnableAsyncExcel {
    String[] basePackages() default {};
}

