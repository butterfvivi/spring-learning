package org.vivi.framework.iexcelbatch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileNameEnum {

    USER_INFO_IMPORT(1, "用户信息", "user", "user.xlsx");

    private final Integer code;
    /**
     * 模板信息
     */
    private final String desc;
    /**
     * 模板路径 必须在template目录下
     */
    private final String dir;
    /**
     * 模板名称
     */
    private final String modelName;

    /**
     * 根据编码查找枚举
     */
//    public static FileNameEnum valueOf(Integer value) {
//        return ArrayUtil.filter(v -> v.getCode().equals(value), FileNameEnum.values());
//    }
}
