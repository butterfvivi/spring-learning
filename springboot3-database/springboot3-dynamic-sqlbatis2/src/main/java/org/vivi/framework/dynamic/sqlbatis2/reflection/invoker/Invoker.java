package org.vivi.framework.dynamic.sqlbatis2.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

public interface Invoker {
    Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

    Class<?> getType();
}
