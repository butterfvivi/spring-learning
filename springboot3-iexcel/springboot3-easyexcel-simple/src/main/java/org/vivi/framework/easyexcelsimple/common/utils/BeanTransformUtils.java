package org.vivi.framework.easyexcelsimple.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanTransformUtils {

    public static <T> T transform(Object source, Class<T> targetClass) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        T targetObj = null;
        try {
            targetObj = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert targetObj != null;
        BeanUtils.copyProperties(source, targetObj);
        return targetObj;
    }

    public static <S, T> List<T> transformList(List<S> source, Class<T> targetClass) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        List<T> targetList = new ArrayList<>();
        for (S sourceObj : source) {
            T targetObj = null;
            try {
                targetObj = targetClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assert targetObj != null;
            BeanUtils.copyProperties(sourceObj, targetObj);
            targetList.add(targetObj);
        }
        return targetList;
    }

    public static <T> Map<String, T> transformMap(Object source, Class<T> targetClass) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(targetClass, "Target class must not be null");

        Map<String, T> target = new HashMap<>(16);
        Class<?> clazz = source.getClass();
        Class<?> superclass = clazz.getSuperclass();

        packageField(source, target, clazz);
        packageField(source, target, superclass);

        return target;
    }

    private static <T> void packageField(Object source, Map<String, T> target, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            T value = null;
            try {
                value = (T) field.get(source);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            target.put(fieldName, value);
        }
    }

}
