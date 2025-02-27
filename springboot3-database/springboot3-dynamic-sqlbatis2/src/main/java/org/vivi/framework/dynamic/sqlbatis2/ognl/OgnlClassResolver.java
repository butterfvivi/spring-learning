package org.vivi.framework.dynamic.sqlbatis2.ognl;

import ognl.DefaultClassResolver;
import org.vivi.framework.dynamic.sqlbatis2.io.Resources;

public class OgnlClassResolver extends DefaultClassResolver {

    @Override
    protected Class<?> toClassForName(String className) throws ClassNotFoundException {
        return Resources.classForName(className);
    }
}
