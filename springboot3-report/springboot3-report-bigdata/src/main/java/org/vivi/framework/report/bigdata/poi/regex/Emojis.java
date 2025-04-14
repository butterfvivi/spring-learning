package org.vivi.framework.report.bigdata.poi.regex;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 魔技表情正则表达式
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Emojis {

    static final String MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS = "[\\uD83C\\uDF00-\\uD83D\\uDDFF]";

    static final String SUPPLEMENTAL_SYMBOLS_AND_PICTOGRAPHS = "[\\uD83E\\uDD00-\\uD83E\\uDDFF]";

    static final String EMOTICONS = "[\\uD83D\\uDE00-\\uD83D\\uDE4F]";

    static final String TRANSPORT_AND_MAP_SYMBOLS = "[\\uD83D\\uDE80-\\uD83D\\uDEFF]";

    static final String MISCELLANEOUS_SYMBOLS = "[\\u2600-\\u26FF]\\uFE0F?";

    static final String DINGBATS = "[\\u2700-\\u27BF]\\uFE0F?";

    static final String ENCLOSED_ALPHANUMERICS = "\\u24C2\\uFE0F?";

    static final String REGIONAL_INDICATOR_SYMBOL = "[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}";

    static final String ENCLOSED_ALPHANUMERIC_SUPPLEMENT = "[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?";

    static final String BASIC_LATIN = "[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3";

    static final String ARROWS = "[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?";

    static final String MISCELLANEOUS_SYMBOLS_AND_ARROWS = "[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?";

    static final String SUPPLEMENTAL_ARROWS = "[\\u2934\\u2935]\\uFE0F?";

    static final String CJK_SYMBOLS_AND_PUNCTUATION = "[\\u3030\\u303D]\\uFE0F?";

    static final String ENCLOSED_CJK_LETTERS_AND_MONTHS = "[\\u3297\\u3299]\\uFE0F?";

    static final String ENCLOSED_IDEOGRAPHIC_SUPPLEMENT = "[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?";

    static final String GENERAL_PUNCTUATION = "[\\u203C\\u2049]\\uFE0F?";

    static final String GEOMETRIC_SHAPES = "[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?";

    static final String LATIN_SUPPLEMENT = "[\\u00A9\\u00AE]\\uFE0F?";

    static final String LETTERLIKE_SYMBOLS = "[\\u2122\\u2139]\\uFE0F?";

    static final String MAHJONG_TILES = "\\uD83C\\uDC04\\uFE0F?";

    static final String PLAYING_CARDS = "\\uD83C\\uDCCF\\uFE0F?";

    static final String MISCELLANEOUS_TECHNICAL = "[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?";

}