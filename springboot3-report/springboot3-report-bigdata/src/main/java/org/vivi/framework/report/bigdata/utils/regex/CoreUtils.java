package org.vivi.framework.report.bigdata.utils.regex;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则核心功能
 * 功能 : 
 * 1.根据正则,判断是否满足正则条件.
 * 2.通过正则,获取匹配的结果集.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CoreUtils {

    /**通过正则,获取匹配的结果集**/
    public static List<String> findMatchs(String source, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return findMatchs(source,pattern);
    }

    /**通过正则,获取匹配的结果集**/
    public static List<String> findMatchs(String source, Pattern pattern) {
        if( StringUtils.isBlank(source) ){ return Lists.newArrayList(); }
        if( null == pattern ){ return Lists.newArrayList(); }
        List<String> matchs = new ArrayList<>();
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String match = matcher.group(i);
                matchs.add(match);
            }
        }
        return matchs;
    }

    /**通过正则,判断源字符串是否匹配**/
    public static boolean doMatch(String source, String regex){
        if( StringUtils.isBlank(regex) ){ return false; }
        if( StringUtils.isBlank(source) ){ return false; }
        Matcher matcher = Pattern.compile(regex).matcher(source);
        if (matcher.find()) { return true; }
        return false;
    }

    /**通过正则,判断源字符串是否匹配**/
    public static boolean doMatch(String source, Pattern pattern){
        if( null == pattern ){ return false; }
        if( StringUtils.isBlank(source) ){ return false; }
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) { return true; }
        return false;
    }

    /**统计匹配的次数**/
    public static Integer matchCount(String source, String regex){
        if( StringUtils.isBlank(source) ){ return 0; }
        if( StringUtils.isBlank(regex) ){ return 0; }
        Pattern pattern = Pattern.compile(regex);
        return matchCount(source,pattern);
    }

    /**统计匹配的次数**/
    public static Integer matchCount(String source, Pattern pattern){
        Matcher matcher = pattern.matcher(source);
        Integer count = 0;
        while(matcher.find()) {
            count ++;
        }
        return count;
    }

}
