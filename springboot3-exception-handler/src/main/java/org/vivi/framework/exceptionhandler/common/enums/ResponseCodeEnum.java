package org.vivi.framework.exceptionhandler.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    /**
     * 成功
     */
    SUCCESS("00000", "成功"),

    USER_ID_NOT_EXIST("10002", "用户id不存在"),

    SYSTEM_EXCEPTION("10001", "系统异常");

    /**
     * 响应编码
     */
    private final String code;

    /**
     * 响应信息
     */
    private final String message;
}
