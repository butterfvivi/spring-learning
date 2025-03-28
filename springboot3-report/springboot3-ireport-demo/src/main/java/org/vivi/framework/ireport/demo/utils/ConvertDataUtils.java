package org.vivi.framework.ireport.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * Convert data tool class
 */
public class ConvertDataUtils {

    public static String objToJsonStr(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    /***
     * check whether obj is empty, if it is a collection, judge the length
     */
    public static boolean checkObjValIsEmpty(Object o) {
        //传入对象为null抛异常
        if (null == o) {
            return true;
        }
        //string为""抛出异常
        if (o instanceof String) {
            return "".equals(o);
        }
        //list长度为0抛出异常
        if (o instanceof List) {
            return ((List) o).size() == 0;
        }
        //map类型长度为0，抛出异常
        if (o instanceof Map) {
            return ((Map) o).size() == 0;
        }
        //如果是Boolean类型，如果值非true抛出异常
        if (o instanceof Boolean) {
            return !(Boolean) o;
        }
        return false;
    }
}
