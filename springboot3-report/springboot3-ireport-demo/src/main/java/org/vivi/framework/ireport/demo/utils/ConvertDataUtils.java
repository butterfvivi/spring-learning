package org.vivi.framework.ireport.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Convert data tool class
 */
public class ConvertDataUtils {

    public static String objToJsonStr(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }
}
