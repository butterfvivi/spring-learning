package org.vivi.framework.ireport.demo.utils;

public class EmptyUtils {


    /**
     * get the length of the string
     **/
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * judge whether the string is empty
     * @param cs
     * @return
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
     * if the value is empty, set the default value
     */
    public static <T> T ifNullSetDefVal(T val, T defVal) {
        //传入对象为null抛异常
        if (null == val) return defVal;
        String strVal = String.valueOf(val);
        if (isEmpty(strVal)) return defVal;
        return val;

    }
}
