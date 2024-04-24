package org.vivi.spring3.security.gateway.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode {

    SUCCESS("00000", "sucess"),
    USERNAME_OR_PASSWORD_ERROR("A0210", "用户名或密码错误"),
    PASSWORD_ENTER_EXCEED_LIMIT("A0211", "用户输入密码次数超限"),
    CLIENT_AUTHENTICATION_FAILED("A0212", "客户端认证失败"),
    INVALID_TOKEN("A0230", "token无效或已过期"),
    TOKEN_ACCESS_FORBIDDEN("A0231", "token已被禁止访问"),

    AUTHORIZED_ERROR("A0300", "访问权限异常"),
    ACCESS_UNAUTHORIZED("A0301", "访问未授权"),
    SYSTEM_EXECUTION_ERROR("B0001", "系统执行出错");

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private String code;

    private String msg;

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }


    public static ResultCode getValue(String code){
        for (ResultCode value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SYSTEM_EXECUTION_ERROR; // 默认系统执行错误
    }
}
