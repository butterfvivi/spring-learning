package org.vivi.framework.dynamic.sqlbatis2;


import org.vivi.framework.dynamic.sqlbatis2.builder.DynamicSqlSource;
import org.vivi.framework.dynamic.sqlbatis2.builder.RawSqlSource;
import org.vivi.framework.dynamic.sqlbatis2.builder.SqlSource;
import org.vivi.framework.dynamic.sqlbatis2.node.TextSqlNode;
import org.vivi.framework.dynamic.sqlbatis2.node.XMLScriptBuilder;
import org.vivi.framework.dynamic.sqlbatis2.parsing.PropertyParser;
import org.vivi.framework.dynamic.sqlbatis2.parsing.XNode;
import org.vivi.framework.dynamic.sqlbatis2.parsing.XPathParser;
import org.vivi.framework.dynamic.sqlbatis2.parsing.xml.XMLMapperEntityResolver;
import org.vivi.framework.dynamic.sqlbatis2.utils.StringUtils;

import java.util.Properties;

/**
 * 动态SQL解析器
 * <p>
 */
public class DynamicSqlParser {
    private DynamicSqlParser() {
    }

    public static SqlSource parserSql(String dynamicSql) {
        final Properties variables = new Properties();
        dynamicSql = StringUtils.Instance.trim(dynamicSql);
        if (dynamicSql.startsWith("<script>")) {
            XPathParser parser = new XPathParser(dynamicSql, false, variables, new XMLMapperEntityResolver());
            return parserSql(parser.evalNode("/script"));
        } else {
            dynamicSql = PropertyParser.parse(dynamicSql, variables);
            TextSqlNode textSqlNode = new TextSqlNode(dynamicSql);
            if (textSqlNode.isDynamic()) {
                return new DynamicSqlSource(textSqlNode);
            } else {
                return new RawSqlSource(dynamicSql);
            }
        }
    }

    public static SqlSource parserSql(XNode script) {
        XMLScriptBuilder builder = new XMLScriptBuilder(script);
        return builder.parseScriptNode();
    }
}
