package org.vivi.framework.drools.model.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
public class Customer implements Serializable {

    private String customerNo;

    private String customerName;

    private Integer customerAge;

    /**
     * 计算命中后的结果
     */
    private Map<String, Integer> factorValueMap;


    /**
     * 分数
     * @param code  分数编号
     * @param factorScore 分数值
     */
    public void addScore(String code, Integer factorScore) {
        factorValueMap.put(code, factorScore);
    }
}