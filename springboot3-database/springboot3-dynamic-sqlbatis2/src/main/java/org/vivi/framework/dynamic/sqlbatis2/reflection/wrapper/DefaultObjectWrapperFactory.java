package org.vivi.framework.dynamic.sqlbatis2.reflection.wrapper;


import org.vivi.framework.dynamic.sqlbatis2.exception.ReflectionException;
import org.vivi.framework.dynamic.sqlbatis2.reflection.MetaObject;

public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new ReflectionException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}
