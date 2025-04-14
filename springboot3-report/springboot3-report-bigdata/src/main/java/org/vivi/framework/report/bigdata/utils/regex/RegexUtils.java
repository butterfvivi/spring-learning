package org.vivi.framework.report.bigdata.utils.regex;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 正则表达式
 * 功能:
 * 1.valid系列,判断是否匹配.
 * 2.contains系列,判断是否包含.
 * 3.matchs系列,返回所有匹配的子串.
 * 4.matchCount系列,返回匹配的次数.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexUtils {

    /**非法字符校验**/
    public static boolean validLegalChar(String source){
        return CoreUtils.doMatch(source, Regex.LEGAL_CHAR.getValue());
    }

    /**校验整数**/
    public static boolean validInteger(String source){
        return CoreUtils.doMatch(source, Regex.INTEGER.getValue());
    }

    /**校验电话号码**/
    public static boolean validPhone(String source){
        return CoreUtils.doMatch(source, Regex.PHONE.getValue());
    }

    /**校验用户名**/
    public static boolean validUserName(String source){
        return CoreUtils.doMatch(source, Regex.USER_NAME_ONE.getValue());
    }

    /**校验字母数字**/
    public static boolean validAlphaAndNumber(String source){
        return CoreUtils.doMatch(source, Regex.ALPHA_NUM.getValue());
    }

    /**是否包含魔技表情**/
    public static boolean containMoji(String source){
        return CoreUtils.doMatch(source, Regex.MOJI_CHAR.getValue());
    }

    /**是否包含中文的校验**/
    public static boolean containChinese(String source) {
        return CoreUtils.doMatch(source, Regex.CHINESE.getValue());
    }

    /**统计问号的匹配次数**/
    public static Integer matchQuestionCount(String source){
        return CoreUtils.matchCount(source, Regex.QUESTION_CHAR.getValue());
    }

    /**统计字符在文本中出现的次数**/
    public static List<String> matchsReturnChar(String source) {
        return CoreUtils.findMatchs(source, Regex.RETURN_CHAR.getValue());
    }
}
