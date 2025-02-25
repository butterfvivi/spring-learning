package org.vivi.framework.sqlbatis;

import org.junit.jupiter.api.Test;
import org.vivi.framework.sqlbatis.handler.SqlBatis;
import org.vivi.framework.sqlbatis.model.SqlResult;

import java.util.HashMap;
import java.util.Map;

/**
 * test for sqlbatis
 */
class SqlBatisTest {

    @Test
    void testParseForSql() {
        String xml = "<select id=\"findUserById\">\n" +
                "           select * from user where 1 = 1 \n" +
                "           <if test=\"id != null\">\n" +
                "               AND id = #{id}\n" +
                "           </if>\n" +
                "           <if test=\"username != null\">\n" +
                "               AND username like ${username}\n" +
                "           </if>\n" +
                "      </select>";
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("username", "'%zhang%'");
        SqlResult sqlResult = SqlBatis.parseForSql(xml, data);

        System.out.println(sqlResult.getFormatSql());
        System.out.println(sqlResult.getSql());
        System.out.println(sqlResult.getParams());
    }

    @Test
    void testParseForSql1() {
        String xml = "<select id=\"findUserById\">\n" +
                "           select * from user where 1 = 1 \n" +
                "           <if test=\"id != null\">\n" +
                "               AND id = #{id}\n" +
                "           </if>\n" +
                "           <if test=\"username != null\">\n" +
                "               AND username in (${username})\n" +
                "           </if>\n" +
                "           <if test=\"age != null\">\n" +
                "               AND age = ${age}\n" +
                "           </if>\n" +
                "      </select>";
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("username", "zhang");
        data.put("age",12);
        SqlResult sqlResult = SqlBatis.parseForSql(xml, data);

        System.out.println(sqlResult.getFormatSql());
        System.out.println(sqlResult.getSql());
        System.out.println(sqlResult.getParams());
    }


}