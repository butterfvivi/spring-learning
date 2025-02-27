package org.vivi.framework.dynamic.sqlbatis2.node;

/**
 * 动态SQL节点
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
