package org.vivi.framework.excel.dynamic1.handler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelHeader {
    /**
     * 要导出的字段名称英文
     */
    private String fieldName;

    /**
     * 要导出的表头名称中文
     */
    private String headName;
    /**
     * 排序
     */
    private Integer order;
    /**
     * 是否展示
     */
    private Boolean display;

}

