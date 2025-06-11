package org.vivi.framework.report.bigdata.general;

import cn.hutool.core.lang.Pair;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
@ConfigurationProperties(prefix = "org.vivi.framework.report.bigdata")
public class ExportExcelManagerProperty {

    private Map<String, ExportExcelManagerTemplate> templates;

    @Data
    public static class ExportExcelManagerTemplate {
        /**
         * 导出的文件名
         */
        private String fileName;

        /**
         * 分页列表接口对应的bean名称
         */
        private String beanName;

        /**
         * 分页列表接口对应的method名称
         */
        private String methodName;

        /**
         * 分页接口对应的请求类
         */
        private String requestBodyClassName;

        /**
         * 分页接口对应的返回类(必须是PageInfo<?>类型)
         */
        private String responseBodyClassName;

        /**
         * 动态配置的header头(由于不同国家输出表格的header头不一样,所以不能直接在dto上打 @ExcelProperty来解决)
         */
        private List<Pair<String, String>> headerFiledMap;
        private String headerFiled;

        /**
         * 动态配置的输出列及转换公式
         */
        private Map<String, String> fieldValueMapping;
        private Map<String, Map<String, String>> fieldValueMappingFunction;
    }

    //@PostConstruct0
    private void init() {
        //
        templates.entrySet().stream()
                .filter(p -> !Strings.isNullOrEmpty(p.getValue().getHeaderFiled()))
                .forEach(p -> {
                    System.out.println(p.getValue().getHeaderFiled());
                    p.getValue().headerFiledMap = toListTuple(p.getValue().getHeaderFiled());
                });

        for (Map.Entry<String, ExportExcelManagerTemplate> entry : templates.entrySet()) {
            Map<String, Map<String, String>> rs = new HashMap<>();
            for (Map.Entry<String, String> entry2 : entry.getValue().getFieldValueMapping().entrySet()) {
                if (!Strings.isNullOrEmpty(entry2.getValue())) {
                    rs.put(entry2.getKey(), toMap(entry2.getValue()));
                }
            }
            entry.getValue().fieldValueMappingFunction = rs;
        }

        System.out.println(this);
    }

    private static List<Pair<String, String>> toListTuple(String str) {
        return toListTuple(str, "&", "=");
    }

    private static List<Pair<String, String>> toListTuple(String str, String itemSpliter, String keyValueSpliter) {
        List<Pair<String, String>> result = new ArrayList<>();
        Arrays.stream(str.split(itemSpliter)).forEach(p -> {
            String[] split = p.split(keyValueSpliter);
            result.add(Pair.of(split[0], split[1]));
        });

        return result;
    }

    private static Map<String, String> toMap(String str) {
        return toMap(str, "&", "=");
    }

    private static Map<String, String> toMap(String str, String itemSpliter, String keyValueSpliter) {
        Map<String, String> result = new TreeMap<>();
        Arrays.stream(str.split(itemSpliter)).forEach(p -> {
            String[] split = p.split(keyValueSpliter);
            result.put(split[0], split[1]);
        });

        return result;
    }
}