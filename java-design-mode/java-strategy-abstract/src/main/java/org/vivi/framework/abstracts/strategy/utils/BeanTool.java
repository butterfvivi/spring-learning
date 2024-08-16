package org.vivi.framework.abstracts.strategy.utils;

public class BeanTool {

    public static  <T> T getBean(Class<T> clazz) {
        String clazzName = clazz.getName();
        clazzName = clazzName.replace(".",",");
        String[] split = clazzName.split(",");
        String name = split[split.length - 1];
        return IocUtil.getBean(lowerFirst(name));
    }

    public static String lowerFirst(String oldStr) {
        char[] chars = oldStr.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

//    public static  <T> T getBean(Class<T> clazz) {
//        //获取类的名字
//        String simpleName = clazz.getSimpleName();
//        //根据类的名称获取类的实列对象
//        return IocUtil.getBean(lowerFirst(simpleName));
//    }
}
