/**
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vivi.framework.sqlbatis.handler;

import org.apache.commons.lang3.StringUtils;
import org.vivi.framework.sqlbatis.mapping.BoundSql;
import org.vivi.framework.sqlbatis.mapping.SqlSource;
import org.vivi.framework.sqlbatis.model.SqlResult;
import org.vivi.framework.sqlbatis.parsing.XNode;
import org.vivi.framework.sqlbatis.parsing.XPathParser;
import org.vivi.framework.sqlbatis.script.xml.XMLScriptBuilder;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * sql batis can process four types sql: select | insert | update | delete
 *
 * @author Laba Zhang
 */
public class SqlBatis {
    /**
     * Supports four SQL types and requires lowercase
     */
    private static final String SQL_TYPE = "select|insert|update|delete";

    public static SqlResult parseForSql(String xml, Map<String, Object> sqlParams) {
        if (StringUtils.isBlank(xml)) {
            throw new NullPointerException("xml must not null");
        }
        return parseForSql(new XPathParser(xml), sqlParams);
    }

    public static SqlResult parseForSql(Reader reader, Map<String, Object> sqlParams) {
        if (reader == null) {
            throw new NullPointerException("reader must not null");
        }
        return parseForSql(new XPathParser(reader), sqlParams);
    }

    public static SqlResult parseForSql(InputStream inputStream, Map<String, Object> sqlParams) {
        if (inputStream == null) {
            throw new NullPointerException("inputStream must not null");
        }
        return parseForSql(new XPathParser(inputStream), sqlParams);
    }

    public static SqlResult parseForSql(Document document, Map<String, Object> sqlParams) {
        if (document == null) {
            throw new NullPointerException("document must not null");
        }
        return parseForSql(new XPathParser(document), sqlParams);
    }

    public static SqlResult parseForSql(XPathParser pathParser, Map<String, Object> sqlParams) {
        return parseForSql(pathParser.evalNode(SQL_TYPE), sqlParams);
    }

    public static SqlResult parseForSql(XNode xNode, Map<String, Object> sqlParams) {
        XMLScriptBuilder builder = new XMLScriptBuilder(xNode);
        SqlSource sqlSource = builder.parseScriptNode();
        BoundSql boundSql = sqlSource.getBoundSql(sqlParams);
        return new SqlResult(boundSql.getSql(), boundSql.getOrderedParams());
    }
}