package org.vivi.framework.generator.ddl.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class JlStrUtil {

    public static String getPathFromRight(String input, int n){
        List<String> parts = Arrays.asList(input.split("\\."));

        if (parts.size() < n){
            return input;
        }
        List<String> collect = parts.stream()
                .limit(parts.size() - n)
                .collect(Collectors.toList());

        return String.join(".", collect);
    }

    public static String makeSurroundWith(String s, String fix){
        String s1 = StrUtil.addPrefixIfNot(s, fix);
        String s2 = StrUtil.addSuffixIfNot(s1, fix);
        return s2;
    }

    /**
     * split指定多个分隔符
     * @param s 要处理的字符串
     * @param separator 多个分隔符入参
     * @return
     */
    public static List<String> split4Separators(String s, String... separator) {
        Integer[] spliter = Arrays.stream(separator).map(s::indexOf).toArray(Integer[]::new);
        int separatorLastIndex = spliter.length - 1;

        List<String> collect = Stream.iterate(0, (index) -> ++index).limit(separatorLastIndex)
                .map(i -> StrUtil.sub(s, spliter[i], spliter[i + 1])).collect(toList());
        int strLastIndex = s.length();
        String sub = StrUtil.sub(s, spliter[separatorLastIndex], strLastIndex);
        collect.add(sub);
        return collect;
    }
}
