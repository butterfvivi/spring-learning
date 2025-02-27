package org.vivi.framework.dynamic.sqlbatis2.reflection.wrapper;


import org.vivi.framework.dynamic.sqlbatis2.reflection.MetaObject;

public interface ObjectWrapperFactory {

    boolean hasWrapperFor(Object object);

    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}
