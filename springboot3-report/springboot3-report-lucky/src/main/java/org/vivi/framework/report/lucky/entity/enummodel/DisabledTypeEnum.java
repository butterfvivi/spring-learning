package org.vivi.framework.report.lucky.entity.enummodel;

/**
 * @description 禁用状态
 */
public enum DisabledTypeEnum {

    /**
     * 启用
     */
    ENABLE(0, "启用"),
    /**
     * 禁用
     */
    DISABLE(1, "禁用"),
    ;

    DisabledTypeEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    /**
     * index
     */
    private final Integer index;

    /**
     * 名称
     */
    private final String name;

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
