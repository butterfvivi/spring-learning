package org.vivi.framework.dynamic.sqlbatis2.node;


import org.vivi.framework.dynamic.sqlbatis2.ognl.OgnlCache;

public class VarDeclSqlNode implements SqlNode {
    private final String name;
    private final String expression;

    public VarDeclSqlNode(String var, String exp) {
        name = var;
        expression = exp;
    }

    @Override
    public boolean apply(DynamicContext context) {
        context.addParameterExpression(expression);
        context.addParameterVar(name);
        final Object value = OgnlCache.getValue(expression, context.getBindings());
        context.bind(name, value);
        return true;
    }
}
