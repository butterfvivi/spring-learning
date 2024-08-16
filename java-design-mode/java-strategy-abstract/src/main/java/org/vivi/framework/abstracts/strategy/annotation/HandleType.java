package org.vivi.framework.abstracts.strategy.annotation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface HandleType {

    String value();
}
