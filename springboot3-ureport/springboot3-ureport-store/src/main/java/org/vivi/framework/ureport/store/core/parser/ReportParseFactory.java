package org.vivi.framework.ureport.store.core.parser;

import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: summer
 * @Date: 2022/8/9 21:23
 * @Description:
 **/
public abstract class ReportParseFactory {

    private static Map<String, Parser> parseMap = new HashMap<>();

    /**
     * 注册解析器
     *
     * @param parser
     */
    public void addParse(Parser parser) {
        if (parser == null || StringUtils.isEmpty(parser.getName())) {
            return;
        }
        ReportParseFactory.parseMap.putIfAbsent(parser.getName(), parser);
    }

    /**
     * 获取解析器
     *
     * @param name
     * @return
     */
    public Parser getParse(String name) {
        return ReportParseFactory.parseMap.get(name);
    }
}
