package org.vivi.framework.dynamic.sqlbatis2.node;

import java.util.Collections;
import java.util.List;

public class SetSqlNode extends TrimSqlNode {
    private static final List<String> COMMA = Collections.singletonList(",");

    public SetSqlNode(SqlNode contents) {
        super(contents, "SET", COMMA, null, COMMA);
    }
}
