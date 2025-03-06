package org.vivi.framework.easyexcel.simple.model.dto;

import java.io.Serializable;

/**
 * @author Cauli
 * @date: 2022/12/1 20:20
 * @description: excel值返回数据模型
 */
public class ScreenValueExcelDTO implements Serializable {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ScreenValueExcelDTO{" +
                "value='" + value + '\'' +
                '}';
    }
}
