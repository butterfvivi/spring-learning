package org.vivi.framework.report.bigdata.poi.regex;

import lombok.Getter;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public enum Regex {
    //
    CHINESE("中文正则", Pattern.compile("^[\\u4e00-\\u9fa5]$")),
    //
    LEGAL_CHAR("非法字符正则校验", Pattern.compile("^[\\u4e00-\\u9fa5\\w\\d_.%@!！\\[\\]\\\\|\\-,/,\\[\\]\\s，。;；:：“”?？]+$")),
    //
    INTEGER("整数", Pattern.compile("^\\d+$")),
    //
    PHONE("电话号码", Pattern.compile("^1[345789]\\d{9}$")),
    //
    ALPHA_NUM("字母正则", Pattern.compile("^[a-zA-Z0-9]+$")),
    //
    USER_NAME_ONE("用户名规则一", Pattern.compile("^(?![0-9]+)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[\\(\\)])+$)([^(0-9a-zA-Z)]|[\\(\\)]|[a-z]|[A-Z]|[0-9]){8,16}$")),
    // 8~16位字符，至少包含数字、字母（区分大小写）、特殊符号中的2种，且不能以数字开头
    RETURN_CHAR("回车换行符正则", Pattern.compile("(\n)")),
    //
    QUESTION_CHAR("问号正则", Pattern.compile("(\\?)")),
    //
    UNICODE_CHAR("unicode正则", Pattern.compile("(\\\\u(\\p{XDigit}{4}))")),
    // 魔技表情正则
    MOJI_CHAR("魔技表情", Pattern.compile("(?:"
            + Emojis.MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS + "|"
            + Emojis.SUPPLEMENTAL_SYMBOLS_AND_PICTOGRAPHS + "|"
            + Emojis.EMOTICONS + "|"
            + Emojis.TRANSPORT_AND_MAP_SYMBOLS + "|"
            + Emojis.MISCELLANEOUS_SYMBOLS + "|"
            + Emojis.DINGBATS + "|"
            + Emojis.ENCLOSED_ALPHANUMERICS + "|"
            + Emojis.REGIONAL_INDICATOR_SYMBOL + "|"
            + Emojis.ENCLOSED_ALPHANUMERIC_SUPPLEMENT + "|"
            + Emojis.BASIC_LATIN + "|"
            + Emojis.ARROWS + "|"
            + Emojis.MISCELLANEOUS_SYMBOLS_AND_ARROWS + "|"
            + Emojis.SUPPLEMENTAL_ARROWS + "|"
            + Emojis.CJK_SYMBOLS_AND_PUNCTUATION + "|"
            + Emojis.ENCLOSED_CJK_LETTERS_AND_MONTHS + "|"
            + Emojis.ENCLOSED_IDEOGRAPHIC_SUPPLEMENT + "|"
            + Emojis.GENERAL_PUNCTUATION + "|"
            + Emojis.GEOMETRIC_SHAPES + "|"
            + Emojis.LATIN_SUPPLEMENT + "|"
            + Emojis.LETTERLIKE_SYMBOLS + "|"
            + Emojis.MAHJONG_TILES + "|"
            + Emojis.PLAYING_CARDS + "|"
            + Emojis.MISCELLANEOUS_TECHNICAL + ")+")),
    ;
    @Getter
    private String name;
    @Getter
    private Pattern value;

    Regex(String name, Pattern value) {
        this.name = name;
        this.value = value;
    }
}
