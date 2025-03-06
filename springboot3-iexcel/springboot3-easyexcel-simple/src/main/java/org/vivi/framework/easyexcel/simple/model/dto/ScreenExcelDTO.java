package org.vivi.framework.easyexcel.simple.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Cauli
 * @date 2022/12/1 9:48
 * @description excel模板参数返回数据模型
 */
@Data
@ToString
public class ScreenExcelDTO implements Serializable {
    /**
     * 模块名称
     */
    private String modelName;

    /**
     * 测试项名称
     */
    private String testItemName;

    /**
     * 测试子项名称
     */
    private String subTestItemName;

    /**
     * 分类名称
     */
    private String testItemCategory;

    /**
     * 测试项值
     */
    private String testItemValue;
}
