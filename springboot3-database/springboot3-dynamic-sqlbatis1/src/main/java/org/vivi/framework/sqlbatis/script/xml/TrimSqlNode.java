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
package org.vivi.framework.sqlbatis.script.xml;

import java.util.*;

/**
 * TrimSqlNode ，实现 SqlNode 接口，<trim /> 标签的 SqlNode 实现类。
 * 在下文中，我们会看到，<trim /> 标签是 <where /> 和 <set /> 标签的基础。
 *
 * <pre>
 *     <trim prefix="WHERE" prefixOverrides="AND | OR ">
 *       ...
 *     </trim>
 *     <update id="testTrim" parameterType="com.mybatis.pojo.User">
 *         update user
 *         set
 *         <trim suffixOverrides="," suffix="where id = #{id},">
 *             <if test="cash!=null and cash!=''">cash= #{cash},</if>
 *             <if test="address!=null and address!=''">address= #{address},</if>
 *         </trim>
 *     </update>
 * </pre>
 *
 */
public class TrimSqlNode implements SqlNode {

    /**
     * 内含的 SqlNode 节点
     */
    private final SqlNode contents;
    /**
     * 前缀
     */
    private final String prefix;
    /**
     * 后缀
     */
    private final String suffix;
    /**
     * 需要被删除的前缀
     */
    private final List<String> prefixesToOverride;
    /**
     * 需要被删除的后缀
     */
    private final List<String> suffixesToOverride;

    public TrimSqlNode(SqlNode contents, String prefix,
                       String prefixesToOverride, String suffix, String suffixesToOverride) {
        this(contents, prefix, parseOverrides(prefixesToOverride), suffix, parseOverrides(suffixesToOverride));
    }

    protected TrimSqlNode(SqlNode contents, String prefix, List<String> prefixesToOverride, String suffix, List<String> suffixesToOverride) {
        this.contents = contents;
        this.prefix = prefix;
        this.prefixesToOverride = prefixesToOverride;
        this.suffix = suffix;
        this.suffixesToOverride = suffixesToOverride;
    }

    @Override
    public boolean apply(DynamicContext context) {
        // <1> 创建 FilteredDynamicContext 对象
        FilteredDynamicContext filteredDynamicContext = new FilteredDynamicContext(context);
        // <2> 执行 contents 的应用
        boolean result = contents.apply(filteredDynamicContext);
        // <3> 执行 FilteredDynamicContext 的应用
        filteredDynamicContext.applyAll();
        return result;
    }

    /**
     * 使用 | 分隔字符串成字符串数组，并都转换成大写。
     *
     * @param overrides
     * @return
     */
    private static List<String> parseOverrides(String overrides) {
        if (overrides != null) {
            final StringTokenizer parser = new StringTokenizer(overrides, "|", false);
            final List<String> list = new ArrayList<>(parser.countTokens());
            while (parser.hasMoreTokens()) {
                list.add(parser.nextToken().toUpperCase(Locale.ENGLISH));
            }
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * FilteredDynamicContext ，是 TrimSqlNode 的内部类，继承 DynamicContext 类，支持 trim 逻辑的 DynamicContext 实现类。
     */
    private class FilteredDynamicContext extends DynamicContext {
        /**
         * 委托的 DynamicContext 对象
         */
        private DynamicContext delegate;
        /**
         * 是否 prefix 已经被应用
         */
        private boolean prefixApplied;
        /**
         * 是否 suffix 已经被应用
         */
        private boolean suffixApplied;
        /**
         * StringBuilder 对象
         *
         * @see #appendSql(String)
         */
        private StringBuilder sqlBuffer;

        public FilteredDynamicContext(DynamicContext delegate) {
            super(null);
            this.delegate = delegate;
            this.prefixApplied = false;
            this.suffixApplied = false;
            this.sqlBuffer = new StringBuilder();
        }

        /**
         * 将 sqlBuffer 处理完后，添加回 delegate.sqlBuffer 中。
         */
        public void applyAll() {
            // 1. trim 掉多余的空格，生成新的 sqlBuffer 对象
            sqlBuffer = new StringBuilder(sqlBuffer.toString().trim());
            // 2. 将 sqlBuffer 大写，生成新的 trimmedUppercaseSql 对象
            String trimmedUppercaseSql = sqlBuffer.toString().toUpperCase(Locale.ENGLISH);
            // 3. 应用 TrimSqlNode 的 trim 逻辑
            if (trimmedUppercaseSql.length() > 0) {
                applyPrefix(sqlBuffer, trimmedUppercaseSql);
                applySuffix(sqlBuffer, trimmedUppercaseSql);
            }
            // 4. 将结果，添加到 delegate 中
            delegate.appendSql(sqlBuffer.toString());
        }

        @Override
        public Map<String, Object> getBindings() {
            return delegate.getBindings();
        }

        @Override
        public void bind(String name, Object value) {
            delegate.bind(name, value);
        }

        @Override
        public int getUniqueNumber() {
            return delegate.getUniqueNumber();
        }

        /**
         * 将拼接的 sql ，暂时存储到 sqlBuffer 中。
         *
         * @param sql 待拼接的SQL
         */
        @Override
        public void appendSql(String sql) {
            sqlBuffer.append(sql);
        }

        @Override
        public String getSql() {
            return delegate.getSql();
        }

        /**
         * 处理prefix
         *
         * @param sql
         * @param trimmedUppercaseSql
         */
        private void applyPrefix(StringBuilder sql, String trimmedUppercaseSql) {
            if (!prefixApplied) {
                prefixApplied = true;
                // prefixesToOverride 非空，先删除
                if (prefixesToOverride != null) {
                    for (String toRemove : prefixesToOverride) {
                        if (trimmedUppercaseSql.startsWith(toRemove)) {
                            sql.delete(0, toRemove.trim().length());
                            break;
                        }
                    }
                }
                // prefix 非空，再添加
                if (prefix != null) {
                    sql.insert(0, " ");
                    sql.insert(0, prefix);
                }
            }
        }

        /**
         * 处理 suffix
         *
         * @param sql
         * @param trimmedUppercaseSql
         */
        private void applySuffix(StringBuilder sql, String trimmedUppercaseSql) {
            if (!suffixApplied) {
                suffixApplied = true;
                // suffixesToOverride 非空，先删除
                if (suffixesToOverride != null) {
                    for (String toRemove : suffixesToOverride) {
                        if (trimmedUppercaseSql.endsWith(toRemove) || trimmedUppercaseSql.endsWith(toRemove.trim())) {
                            int start = sql.length() - toRemove.trim().length();
                            int end = sql.length();
                            sql.delete(start, end);
                            break;
                        }
                    }
                }
                // suffix 非空，再添加
                if (suffix != null) {
                    sql.append(" ");
                    sql.append(suffix);
                }
            }
        }

    }

}
