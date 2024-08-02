package org.vivi.framework.iexcelsimple.toolkit.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  * 重写默认的excel导出方法：先在class类上加上@MsAsync("p1"),然后在自定义方法上加上@MsAsync("p2"),
 *  * 动态导出：m(List<Map<String,Object>> data,List<String> headers)
 *  * 模板：m(List<Map<String,Object>> data, Map<String,Object> otherVal)
 *  * 导入：m(List<?> data)或m(List<?> data,String remark)
 *  *
 */

@Component
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IExcelRewrite {

    //目标名称。如果前台传入targetParam 值为"className@dynamic"，请求重写的类上加上@MsAsync("className"),方法上加上@MsAsync("dynamic")
    String targetParam();

    //导入所需，加在方法体上，如果有值，那么方法体可以用这个List<class>接受数据，不填必须用List<Map>接受
    Class<?> entityClass() default Void.class;

    //重写水印,加方法体上，waterMarkClass必须实现IExcelWaterMark接口
    Class<? extends IExcelWaterMark> waterMarkClass() default IExcelWaterMark.class;
}
