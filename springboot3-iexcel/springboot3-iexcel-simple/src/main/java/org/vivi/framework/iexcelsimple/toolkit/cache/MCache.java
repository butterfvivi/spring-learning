package org.vivi.framework.iexcelsimple.toolkit.cache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MCache {

    //缓存第一次解析含有@MsExcelRewrite类上注解的class信息,key=targetParam.split("@")[0],value = 解析到的类.class
    public static Map<String, Class<?>> excelClassCache = new ConcurrentHashMap<>();

    //缓存第一次解析到class类中含有@MsExcelRewrite注解的方法信息， key=targetParam,value = 解析到的类中的方法对象
    public static Map<String, Method> excelMethodCache = new ConcurrentHashMap<>();

    //缓存导入的所需要映射的实体
    public static Map<String, Class<?>> excelImportCache = new ConcurrentHashMap<>();

    //这个是控制整个项目jar包是否能正常使用的东西
    public static Map<String, String> othersCache = new ConcurrentHashMap<>();
}
