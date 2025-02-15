package org.vivi.framework.generator.ddl.translate.translator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.isBlank;

public class TransUtil {

    private static final String APP_ID = "20221102001431448";
    private static final String SECURITY_KEY = "jDe2X9lkQtXnuSuXtd5V";

    /**
     * 翻译成下划线
     *
     * @param chinese 中国人 入参
     * @return {@link String }
     * @author addzero
     * @since 2022/11/09
     */
    public static String chinese2English(String chinese) {
        return chinese2English(chinese, CaseConfig.下划线);
    }

    /**
     * 翻译成下划线
     *
     * @param chinese 中国人 入参
     * @return {@link String }
     * @author addzero
     * @since 2022/11/09
     */
    public static String chinese2English(String chinese, CaseConfig caseConfig) {
        if (isBlank(chinese)) {
            return "";
        }
        String apply = null;
        try {
            TransApi api = new TransApi(APP_ID, SECURITY_KEY);
            String transResult = api.getTransResult(chinese, "auto", "en");
            TransOutVO transOutVO = JSON.parseObject(transResult, TransOutVO.class);

            List<TransResultDTO> transResultVO = transOutVO.getTransResult();
            //这里是百度翻译QPS限制1s访问一次,transResultVO会空
            while (CollUtil.isEmpty(transResultVO)) {
                ThreadUtil.sleep(1, TimeUnit.SECONDS);
                String transResult2 = api.getTransResult(chinese, "auto", "en");
                TransOutVO transOutVO2 = JSON.parseObject(transResult2, TransOutVO.class);
                List<TransResultDTO> transResultVO2 = transOutVO2.getTransResult();
                transResultVO = transResultVO2;
            }

            String dst = transResultVO.stream().map(TransResultDTO::getDst).findAny().orElse("");
            apply = caseConfig.getTransFunction().apply(dst);
        } catch (Exception e) {
            String firstLetter = PinyinUtil.getFirstLetter(chinese, "");
            return firstLetter;
        }
        return apply;
    }

    /**
     * 批量翻译成下划线英文
     *
     * @param chinese 中国人 入参
     * @return {@link List }<{@link String }>
     * @author addzero
     * @since 2022/11/09
     */
    public static List<String> chinese2English(List<String> chinese) {
        return chinese.stream().map(e -> toUnderLine(chinese2English(e))).collect(Collectors.toList());
    }

    public static String chinese2EnglishByInputColumn(final String chinese) {
        return chinese2EnglishByInput(chinese, System.lineSeparator());
    }

    public static String chinese2EnglishByInputRow(final String chinese) {
        return chinese2EnglishByInput(chinese, "\t");
    }

    private static String chinese2EnglishByInput(String chinese, final String lineSeparator) {
        List<String> split = StrUtil.split(chinese, lineSeparator);
        List<String> strings = chinese2English(split);
        String collect = strings.stream().collect(Collectors.joining(lineSeparator));
        return collect;
    }

    private static String toUpperCase(CharSequence charSequence) {
        String s = toUnderLine(charSequence);
        String s1 = StrUtil.toCamelCase(s);
        String s2 = StrUtil.upperFirst(s1);
        return s2;
    }

    private static String toCamelCase(CharSequence charSequence) {
        String s = toUnderLine(charSequence);
        return StrUtil.toCamelCase(s);
    }

    private static String toUnderLine(CharSequence s) {
        String replace = StrUtil.replace(s, " ", "_", true);
        String s1 = StrUtil.toUnderlineCase(replace);
        return s1;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public enum CaseConfig {
        小驼峰(TransUtil::toCamelCase), 大驼峰(TransUtil::toUpperCase), 下划线(TransUtil::toUnderLine);
        Function<CharSequence, String> transFunction;
    }

}
