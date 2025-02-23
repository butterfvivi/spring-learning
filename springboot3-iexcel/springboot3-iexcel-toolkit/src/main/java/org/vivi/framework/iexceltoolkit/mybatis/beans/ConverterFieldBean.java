package org.vivi.framework.iexceltoolkit.mybatis.beans;

import java.util.Map;
import java.util.Set;

/**
 * 过滤器模型
 */
public class ConverterFieldBean {

    /**
     * 数据源匹配
     * 比如自定义情况下
     * id->1,name->张三
     */
    private Map<String, String> sourceMap;
    /**
     * 数据源匹配
     * 比如自定义情况下
     * 父子类型等的特殊情况
     */
    private Map<String, Map<String, String>>  sourceLinkedMap;

    /**
     * 数据源匹配set
     */
    private Set<String> sourceLinkedSet;

    /**
     * 数据源匹配字符串
     */
    private String sourceLinkedStr;


    public Set<String> getSourceLinkedSet() {
        return sourceLinkedSet;
    }

    public void setSourceLinkedSet(Set<String> sourceLinkedSet) {
        this.sourceLinkedSet = sourceLinkedSet;
    }

    public String getSourceLinkedStr() {
        return sourceLinkedStr;
    }

    public void setSourceLinkedStr(String sourceLinkedStr) {
        this.sourceLinkedStr = sourceLinkedStr;
    }

    public Map<String, String> getSourceMap() {
        return sourceMap;
    }

    public void setSourceMap(Map<String, String> sourceMap) {
        this.sourceMap = sourceMap;
    }

    public Map<String, Map<String, String>> getSourceLinkedMap() {
        return sourceLinkedMap;
    }

    public void setSourceLinkedMap(Map<String, Map<String, String>> sourceLinkedMap) {
        this.sourceLinkedMap = sourceLinkedMap;
    }

    @Override
    public String toString() {
        return "ConverterFieldBean{" +
                "sourceMap=" + sourceMap +
                ", sourceLinkedMap=" + sourceLinkedMap +
                ", sourceLinkedSet=" + sourceLinkedSet +
                ", sourceLinkedStr='" + sourceLinkedStr + '\'' +
                '}';
    }
}
