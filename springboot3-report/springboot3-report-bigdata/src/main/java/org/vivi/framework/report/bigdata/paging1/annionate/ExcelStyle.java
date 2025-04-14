package org.vivi.framework.report.bigdata.paging1.annionate;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.annotation.*;

/**
 * @author xuteng
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelStyle {

    String fontName() default "宋体";

    short fontHeightInPoints() default 12;

    HorizontalAlignment horizontalAlignment() default HorizontalAlignment.CENTER;

    VerticalAlignment verticalAlignment() default VerticalAlignment.CENTER;

    boolean wrapText() default false;

    boolean richText() default false;

    boolean isRequire() default false;
}

