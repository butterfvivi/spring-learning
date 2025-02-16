package org.vivi.framework.excel.configure.base.annotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportField {
    /**
     * 表格列名描述
     *
     * @return
     */
    String title() default "";

    /**
     * 表格列名索引位置,从0开始
     * @return
     */
    int    index() default 0;

    /**
     * 表格字段值来源
     * @return
     */
    String sourceKey() default "";

    /**
     * 日期类型转换
     * @return
     */
    String formate() default "";

    /**
     * 保留源字段和替换字段,比如想保留id,同时在id后面加入name，
     * 则id.index=1,id.referIndex=2
     * name.index=2,name.referIndex=-1
     * @return
     */
    int referIndex() default -1;



}
