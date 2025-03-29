package org.vivi.framework.ireport.demo.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rewrite the default excel export method: first add @MsExcelRewrite("p1") to the class, and then add @MsExcelRewrite("p2") to the custom method,
 * dynamic export: m(List<Map<String,Object>> data,List<String> headers)
 * template: m(List<Map<String,Object>> data, Map<String,Object> otherVal)
 * import: m(List<?> data) or m(List<?> data,String remark)
 */
@Component
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IExcelRewrite {

    /**
     * target name. If the front end passes in targetParam value is 'className@dynamic',
     * the class to be rewritten is annotated with @MsExcelRewrite('className'), and the method is annotated with @MsExcelRewrite('dynamic')
     */
    String targetParam();
    /**
     * import required, added to the method body, if there is a value, then the method body can use this List<class> to receive data,
     * List<Map> must be used if not filled
     */
    Class<?> entityClass() default Void.class;

}

