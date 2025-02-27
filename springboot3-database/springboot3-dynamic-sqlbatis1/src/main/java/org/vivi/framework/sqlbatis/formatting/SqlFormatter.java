package org.vivi.framework.sqlbatis.formatting;

/**
 * 将SQL进行格式化
 */
public class SqlFormatter {

    /**
     * 移除换行及多余空格
     *
     * @param sql sql
     * @return 处理后的SQL
     */
    public static String format(String sql) {
        sql = sql.replaceAll("[\\s\n ]+", " ");
        return sql;
    }
}