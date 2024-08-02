package org.vivi.framework.iexcelsimple.common.utils;

import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 存放一些日常中常用的数据互转、数据拷贝等
 */
public class ConvertDataUtils {
    //保留2-3位小数
    private static DecimalFormat decimalFormat2 = new DecimalFormat("#.00");
    private static DecimalFormat decimalFormat3 = new DecimalFormat("#.000");


    //TODO List<Bean>转换List<Map>
    public static <T> List<Map<String, Object>> listBeanToListMap(List<T> listBean) {
        return listBean.stream().map(t -> (Map<String, Object>) parseObject(objToJsonStr(t), Map.class)).collect(Collectors.toList());
    }

    //TODO 蒋String类型的{"one":"1","two":"2"},{"three":"3","four":"4"}转换成Map<String,Object>
    public static Map<String, Object> strToMap(String str) {
        return JSONObject.parseObject(str, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * 把string类型的json转换成任意List集合数据
     **/
    public static <T> List strToList(String str, Class<T> tClass) {
        JSONArray jsonArray = JSON.parseArray(str);
        return jsonArray.toJavaList(tClass);
    }

    /***
     * 对象转换成json字符串，保留空属性key
     * */
    public static String objToJsonStr(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 驼峰转带下划线的，比如userName=>user_name
     **/
    public static String camLToUnderline(String str) {
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串
     */
    public static String lineToCamel(String str) {
        // 快速检查
        if (str == null || str.isEmpty() || !str.contains("_")) {
            // 没必要转换
            return str;
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                flag = true;
            } else {
                if (flag) {
                    sb.append(Character.toUpperCase(c));
                    flag = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }


    /**
     * 去除字符串最后一位数
     */
//    public static String delStrLastDigitSymbol(String str) {
//        if (StringUtils.isBlank(str)) return str;
//        String newStr = str.substring(0, str.length() - 1);
//        return newStr;
//    }

    /**
     * double类型数字保留两位小数
     *
     * @param dou
     * @return 返回string
     * @author mashuai
     */

    public static String doubleKeepDecimalStr2(Double dou) {
        if (dou == null) return null;
        return String.format("%.2f", dou);
    }

    public static String doubleKeepDecimalStr3(Double dou) {
        if (dou == null) return null;
        return String.format("%.3f", dou);
    }

    /**
     * Double类型数字，保留小数
     *
     * @param dou      数字
     * @param newScale 保留的位数
     * @return 返回Double类型，如果结果是0.85格式，展示在前台会变成.85
     * @author mashuai
     */

    public static Double doubleKeepDecimal(Double dou, Integer newScale) {
        BigDecimal bigDecimal = new BigDecimal(dou).setScale(newScale, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static String objKeepKeepDecimal2(Object val) {
        return decimalFormat2.format(val);
    }

    public static String objKeepKeepDecimal3(Object val) {
        return decimalFormat3.format(val);
    }

    /**
     * 把一个list集合，按照指定len拆分。
     * 一般可以用于一个大的list，把他拆分成len组，每组单独丢入线程池中执行
     *
     * @param list
     * @param len
     * @return List<List>
     * @author mashuai
     */

    public static <T> List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    /**
     * 把集合ID拼接成('a1','a2'....)
     *
     * @author mashuai
     */
    public static String joinInSql(List collect) {
        StringBuilder sql = new StringBuilder();
        sql.append("  ( ");
        for (int i = 0; i < collect.size(); i++) {
            if (i < collect.size() - 1) {
                sql.append("'");
                sql.append(collect.get(i));
                sql.append("', ");
            } else {
                sql.append("'");
                sql.append(collect.get(i));
                sql.append("' ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * 把url的？后面的&参数，转成一个map数字
     *
     * @param url
     * @return
     * @author mashuai
     */

    public static Map<String, String> parseURLParameters(String url) {
        Map<String, String> paramMap = new HashMap<>();
        if (EmptyUtils.isEmpty(url)) return paramMap;
        String[] params = url.split("\\?")[1].split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    paramMap.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return paramMap;
    }

    /**
     * 检查一个参数是否是基本数据类型的包装类型
     *
     * @param arg
     * @return
     * @author mashuai
     */

    public static boolean paramIsBasicType(Object arg) {
        return (arg instanceof Integer || arg instanceof Long || arg instanceof String
                || arg instanceof Float || arg instanceof Double || arg instanceof Short
                || arg instanceof Character || arg instanceof Byte);
    }


    /**
     * 生成指定长度的验证妈
     *
     * @param length
     * @return
     * @author mashuai
     */

    public static String generateRandomCode(int length) {
        // 可供选择的字符
        String characters = "0123456789";
        // 创建一个Random对象
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        // 生成指定长度的验证码
        for (int i = 0; i < length; i++) {
            // 从可供选择的字符中随机选择一个字符加入验证码
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }

    /***
     *截取字符串，并获取截取后最后一个字符串
     * @param str
     * @param symbol
     * @return
     * @author mashuai
     */

    public static String getSplitStrEnd(String str, String symbol) {
        if (EmptyUtils.isEmpty(str) || EmptyUtils.isEmpty(symbol)) return str;
        if (".".equals(symbol)) {
            symbol = "\\.";
        } else if (" ".equals(symbol)) {
            symbol = "\\s+";
        }
        String[] split = str.split(symbol);
        return split[split.length - 1];
    }

    /***
     *截取字符串，并获取截取后指定位置一个字符串
     * @param str
     * @param symbol
     * @param index
     * @return
     * @author mashuai
     */
    public static String getSplitStrIndex(String str, String symbol, Integer index) {
        if (EmptyUtils.isEmpty(str)) return str;
        String[] split = str.split(symbol);
        return split[index];
    }


    /***
     * 检测obj类型的值是否为空，如果是集合判断长度是否为空
     * @param o
     * @return:为null返回true，否则返回false
     * @author:mashuai
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
