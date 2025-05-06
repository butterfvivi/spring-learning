package org.vivi.framework.report.bigdata.general;

import java.lang.reflect.Field;

public class ReflectionUtils {

    /**
     * 获取对象中指定字段的值，支持私有字段
     *
     * @param obj       对象实例
     * @param fieldName 字段名称
     * @return 字段值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        }

        Class<?> clazz = obj.getClass();
        Field field = null;

        // 查找当前类及其父类中的字段
        while (clazz != null && !clazz.equals(Object.class)) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        if (field == null) {
            throw new RuntimeException("字段 " + fieldName + " 不存在于对象 " + obj.getClass());
        }

        try {
            field.setAccessible(true); // 允许访问私有字段
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法访问字段：" + fieldName, e);
        }
    }
}