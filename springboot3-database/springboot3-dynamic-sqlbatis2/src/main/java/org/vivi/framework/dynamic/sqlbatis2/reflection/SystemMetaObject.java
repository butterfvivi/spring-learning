package org.vivi.framework.dynamic.sqlbatis2.reflection;


import org.vivi.framework.dynamic.sqlbatis2.reflection.factory.DefaultObjectFactory;
import org.vivi.framework.dynamic.sqlbatis2.reflection.factory.ObjectFactory;
import org.vivi.framework.dynamic.sqlbatis2.reflection.wrapper.DefaultObjectWrapperFactory;
import org.vivi.framework.dynamic.sqlbatis2.reflection.wrapper.ObjectWrapperFactory;

public final class SystemMetaObject {
    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());

    private SystemMetaObject() {
    }

    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
    }
}
