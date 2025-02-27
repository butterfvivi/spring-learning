package org.vivi.framework.dynamic.sqlbatis2.builder;


public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
