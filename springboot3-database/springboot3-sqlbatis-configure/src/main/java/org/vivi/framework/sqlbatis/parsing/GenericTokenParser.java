/**
 * Copyright 2020-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vivi.framework.sqlbatis.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的 Token 解析器
 */
public class GenericTokenParser {

    /**
     * 开始的 Token 字符串
     */
    private final String openToken;
    /**
     * 结束的 Token 字符串
     */
    private final String closeToken;
    private final TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }

    public String parse(String text) {
        return parseForResult(text).getSql();
    }

    /**
     * ${alias} => 将动态值替换为具体值
     *
     * @param text 需要替换的数据
     * @return 替换后的数据
     */
    public TokenResult parseForResult(String text) {
        if (text == null || text.isEmpty()) {
            return new TokenResult("");
        }
        // search open token
        // 寻找开始的 openToken 的位置
        int start = text.indexOf(openToken);
        if (start == -1) {
            return new TokenResult(text);
        }

        List<String> placeHolderParams = new ArrayList<>();
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        // 匹配到 openToken 和 closeToken 之间的表达式
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                // 因为 openToken 前面一个位置是 \ 转义字符，所以忽略 \
                // 添加 [offset, start - offset - 1] 和 openToken 的内容，添加到 builder 中
                builder.append(src, offset, start - offset - 1).append(openToken);
                // 修改 offset
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                // 创建/重置 expression 对象
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                // 添加 offset 和 openToken 之间的内容，添加到 builder 中
                builder.append(src, offset, start - offset);
                // 修改 offset
                offset = start + openToken.length();
                // 寻找结束的 closeToken 的位置
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    // 转义
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        // 因为 endToken 前面一个位置是 \ 转义字符，所以忽略 \
                        // 添加 [offset, end - offset - 1] 和 endToken 的内容，添加到 builder 中
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        // 修改 offset
                        offset = end + closeToken.length();
                        // 继续，寻找结束的 closeToken 的位置
                        end = text.indexOf(closeToken, offset);
                        // 非转义
                    } else {
                        // 添加 [offset, end - offset] 的内容，添加到 builder 中
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                // 拼接内容
                if (end == -1) {
                    // close token was not found.
                    // closeToken 未找到，直接拼接
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    // closeToken 找到，将 expression 提交给 handler 处理 ，并将处理结果添加到 builder 中
                    builder.append(handler.handleToken(expression.toString()));
                    placeHolderParams.add(expression.toString());
                    // 修改 offset
                    offset = end + closeToken.length();
                }
            }
            // 继续，寻找开始的 openToken 的位置
            start = text.indexOf(openToken, offset);
        }
        // 拼接剩余的部分
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return new TokenResult(builder.toString(), placeHolderParams);
    }

    public static class TokenResult {
        private String sql;
        private List<String> placeHolderParams;

        public TokenResult(String sql) {
            this.sql = sql;
            placeHolderParams = new ArrayList<>();
        }

        public TokenResult(String sql, List<String> placeHolderParams) {
            this.sql = sql;
            this.placeHolderParams = placeHolderParams;
        }

        public String getSql() {
            return sql;
        }

        public List<String> getPlaceHolderParams() {
            return placeHolderParams;
        }
    }
}
