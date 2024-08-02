package org.vivi.framework.iexcelsimple.common.utils;

import java.util.Collection;
import java.util.Map;

public class EmptyUtils {


    /**
     * 字符串不为空判断
     *
     * @param cs
     * @return
     * @author mashuai
     */

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }


    /**
     * 字符串为空判断
     *
     * @param cs
     * @return
     * @author mashuai
     */

    public static boolean isEmpty(CharSequence cs) {
        int strLen = length(cs);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 如果值为空就设置默认值
     *
     * @param val
     * @param defVal
     * @return
     * @author mashuai
     */

    public static <T> T ifNullSetDefVal(T val, T defVal) {
        //传入对象为null抛异常
        if (null == val) return defVal;
        String strVal = String.valueOf(val);
        if (isEmpty(strVal)) return defVal;
        return val;

    }

    /**
     * 获取字符串长度
     **/
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * 判断集合长度是否为0
     **/
    public static boolean isCollectZero(Collection collection) {
        if (collection == null || collection.size() == 0) return true;
        return false;
    }


    /**
     * 如果map长度为0或null返回true
     **/
    public static boolean isCollectZero(Map map) {
        if (map == null || map.size() == 0) return true;
        return false;
    }
}
