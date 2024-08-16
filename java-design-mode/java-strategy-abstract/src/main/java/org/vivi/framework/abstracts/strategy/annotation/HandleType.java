package org.vivi.framework.abstracts.strategy.annotation;


import java.lang.annotation.*;

/**
 * 自定义类级别注解，用来区分不同操作的标识，
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface HandleType {

    String value();
}
