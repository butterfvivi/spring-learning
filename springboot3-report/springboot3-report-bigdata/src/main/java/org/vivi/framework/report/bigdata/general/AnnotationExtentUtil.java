package org.vivi.framework.report.bigdata.general;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnnotationExtentUtil {

    /**
     * 获取 clazz 类及其父类中所有带有指定注解的字段，并返回字段与注解的映射
     *
     * @param clazz           目标类
     * @param annotationClass 注解类型
     * @param <A extends Annotation>
     * @return Map<Field, A> 字段与注解的映射
     */
    public static <A extends Annotation> Map<Field, A> getAllField(Class<?> clazz, Class<A> annotationClass) {
        Map<Field, A> fieldMap = new LinkedHashMap<>();

        // 遍历当前类及所有父类
        while (clazz != null && !clazz.equals(Object.class)) {
            for (Field field : clazz.getDeclaredFields()) {
                A annotation = field.getAnnotation(annotationClass);
                if (annotation != null) {
                    field.setAccessible(true); // 允许访问私有字段
                    fieldMap.put(field, annotation);
                }
            }
            clazz = clazz.getSuperclass();
        }

        return fieldMap;
    }
}
