package org.vivi.framework.lucky.simple.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class JsonUtil {
    static final Gson GSON=new Gson();
    static final Gson GSONData=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    static final Gson GSONDataTime=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static String toJson(Object o){
        return GSON.toJson(o);
    }
    public static String toJsonByDate(Object o){
        return GSONData.toJson(o);
    }
    public static String toJsonByDateTime(Object o){
        return GSONDataTime.toJson(o);
    }

    public static void main(String[]args){
        String str="{\"day1\":\"2019-10-10\",\"day2\":null}";
        Map<String,Date> map=GSONData.fromJson(str,Map.class);


    }


}
