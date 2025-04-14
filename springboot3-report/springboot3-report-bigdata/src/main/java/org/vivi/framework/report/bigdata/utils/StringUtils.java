package org.vivi.framework.report.bigdata.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

    public static final String DEFAULT_SEPERATOR = ",";
    public static final String SEPERATOR_COMMA = DEFAULT_SEPERATOR;
    public static final String SEPERATOR_VERTICAL_BAR = "\\|";
    public static final String SEPERATOR_SEMICOLON = ";";


    /**指定分割付的字符串,转换为list**/
    public static List<String> splitToList(String source){
        return splitToList(source,DEFAULT_SEPERATOR);
    }

    /**指定分割符的字符串,转换为list**/
    public static List<String> splitToList(String source, String seperator){
        if(null == seperator){
            seperator = DEFAULT_SEPERATOR;
        }
        if( StringUtils.isBlank(source) ){
            return Lists.newArrayList();
        }
        return Splitter.on(seperator).trimResults().splitToList(source);
    }

    /**字符串格式化**/
    public static String formatJson(String content) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int count = 0;
        while(index < content.length()){
            char ch = content.charAt(index);
            if(ch == '{' || ch == '['){
                sb.append(ch);
                sb.append('\n');
                count++;
                appendTabs(sb, count);
            }else if(ch == '}' || ch == ']'){
                sb.append('\n');
                count--;
                appendTabs(sb, count);
                sb.append(ch);
            }else if(ch == ','){
                sb.append(ch);
                sb.append('\n');
                appendTabs(sb, count);
            }else {
                sb.append(ch);
            }
            index ++;
        }
        return sb.toString();
    }

    /**追加tab键**/
    private static void appendTabs(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append('\t');
        }
    }

}
