package org.vivi.framework.ireport.demo.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    public static Map<String, Object> getViewParams(JSONArray jsonArray)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if(jsonArray != null)
        {
            Map<String, JSONObject> prefixMap = new HashMap<String, JSONObject>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject param = jsonArray.getJSONObject(i);
                boolean isDefault = param.getBooleanValue("isDefault");
                String key = param.getString("paramCode");
                String prefix = param.getString("paramPrefix");//参数前缀 api请求才有可能会用到
                JSONObject paramObj = null;
                if(StringUtils.isNotEmpty(prefix)) {
                    if(prefixMap.containsKey(prefix)) {
                        paramObj = prefixMap.get(prefix);
                    }else {
                        paramObj = new JSONObject();
                        String[] attrs = prefix.split("\\.");
                        if(attrs.length > 1) {
                            JSONObject temp = null;
                            for (int j = 0; j < attrs.length; j++) {
                                if(j == 0) {
                                    if(result.containsKey(attrs[j])) {
                                        temp = (JSONObject) result.get(attrs[j]);
                                        continue;
                                    }
                                    JSONObject jsonObject = new JSONObject();
                                    result.put(attrs[j], jsonObject);
                                    temp = jsonObject;
                                }else if(j == attrs.length - 1) {
                                    temp.put(attrs[j], paramObj);
                                }else {
                                    if(temp.containsKey(attrs[j])) {
                                        temp = (JSONObject) temp.get(attrs[j]);
                                        continue;
                                    }
                                    JSONObject jsonObject = new JSONObject();
                                    temp.put(attrs[j], jsonObject);
                                    temp = jsonObject;
                                }
                            }
                        }else {
                            result.put(attrs[0], paramObj);
                        }
                        prefixMap.put(prefix, paramObj);
                    }
                }
                Object value = param.get(key);
                if(paramObj != null) {
                    paramObj.put(key, value);
                }else {
                    result.put(key, value);
                }
            }
        }
        return result;
    }
}
