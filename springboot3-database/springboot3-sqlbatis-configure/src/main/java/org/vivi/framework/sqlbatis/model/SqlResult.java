
package org.vivi.framework.sqlbatis.model;


import org.vivi.framework.sqlbatis.formatting.SqlFormatter;

import java.util.Map;

/**
 * After SQL is processed, ${} is replaced with the real value, #{} is replaced with? Placeholder,
 * and #{} represents the parameters, stored in params in the order of appearance in SQL.
 *
 */
public class SqlResult {
    /**
     * processed sql (may be with ?)
     */
    private String sql;
    /**
     * ordered param, the order according to the order of #{xxx} parameter in sql
     * {
     * 1: "value1",
     * 2: 2,
     * 3: true,
     * 4: 3.1415926,
     * }
     * the start number is 1, because the index of param in PrepareStatement sql is start from 1
     */
    private Map<Integer, Object> params;

    public SqlResult(String sql, Map<Integer, Object> params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public String getFormatSql() {
        return SqlFormatter.format(this.sql);
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, Object> getParams() {
        return params;
    }

    public void setParams(Map<Integer, Object> params) {
        this.params = params;
    }
}