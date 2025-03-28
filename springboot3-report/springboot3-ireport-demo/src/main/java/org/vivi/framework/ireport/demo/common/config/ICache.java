package org.vivi.framework.ireport.demo.common.config;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * store all cache information of the toolkit
 */
public class ICache {

    //缓存第一次解析含有@MsExcelRewrite类上注解的class信息,key=targetParam.split("@")[0],value = 解析到的类.class
    /**
     * cache the first time to parse the class information containing the @IExcelRewrite class annotation
     * key=targetParam.split("@")[0],value = the class.class that has been parsed
     */
    public static Map<String, Class<?>> excelClassCache = new ConcurrentHashMap<>();

    //缓存第一次解析到class类中含有@MsExcelRewrite注解的方法信息， key=targetParam,value = 解析到的类中的方法对象
    /**
     * cache the first time to parse the method information containing the @IExcelRewrite annotation in the class
     * key=targetParam,value = the method object in the class that has been parsed
     */
    public static Map<String, Method> excelMethodCache = new ConcurrentHashMap<>();

    /**
     * cache the entity that needs to be mapped for import
     */
    public static Map<String, Class<?>> excelImportCache = new ConcurrentHashMap<>();
}
