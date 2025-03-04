package org.vivi.framework.report.simple.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

    public static Object getValue(Object o, String target) {
        try {
            Field declaredField = o.getClass().getDeclaredField(target);
            declaredField.setAccessible(true);
            return declaredField.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getSuperValue(Object o, String target) {
        try {
            Field declaredField = o.getClass().getSuperclass().getDeclaredField(target);
            declaredField.setAccessible(true);
            return declaredField.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给具体的赋值
     *
     * @param o
     * @param fieldName 字段名称
     * @param value
     */
    public static void setValue(Object o, String fieldName, Object value) {
        try {
            Field declaredField = o.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(o, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的字段
     *
     * @param from
     * @return
     */
    public static List<Field> getAllFields(Object from) {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = from.getClass();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

    /**
     * 获取所有的字段
     *
     * @param tempClass
     * @return
     */
    public static List<Field> getAllFields(Class tempClass) {
        List<Field> fieldList = new ArrayList<>();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

    /**
     * 获取所有的字段ReflectionUtils
     *
     * @param from
     * @return
     */
    public static Field[] getAllFieldsArr(Object from) {
        List<Field> fieldList = getAllFields(from);
        return fieldList.stream().toArray(Field[]::new);
    }

    /**
     * 获取所有的字段
     *
     * @param tempClass
     * @return
     */
    public static Field[] getAllFieldsArr(Class tempClass) {
        List<Field> fieldList = getAllFields(tempClass);
        return fieldList.stream().toArray(Field[]::new);

    }

    /**
     * 根据字段名称获取字段
     *
     * @param tempClass
     * @param field
     * @return
     */
    public static Field getField(Class tempClass, String field){
        Field declaredField = null;
        try {
            declaredField = tempClass.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            try {
                declaredField = tempClass.getSuperclass().getDeclaredField(field);
            }catch (NoSuchFieldException ex){

            }
        }
        return declaredField;
    }

    /**
     * 根据字段名称获取字段
     *
     * @param object
     * @param field
     * @return
     * @throws NoSuchFieldException
     */
    public static Field getField(Object object, String field) {
        Class<?> tempClass = object.getClass();
        return getField(tempClass,field);
    }
}

