package org.vivi.framework.dynamic.sqlbatis2.builder;


import org.vivi.framework.dynamic.sqlbatis2.node.DynamicContext;
import org.vivi.framework.dynamic.sqlbatis2.node.SqlNode;

public class RawSqlSource implements SqlSource {
    private final SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        this(getSql(rootSqlNode));
    }

    public RawSqlSource(String sql) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(null);
        sqlSource = sqlSourceParser.parse(sql);
    }

    private static String getSql(SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }
}
