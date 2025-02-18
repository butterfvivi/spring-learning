package org.vivi.framework.report.simple.common.exception;

public class BusinessException extends RuntimeException{
    /**
     * 异常码
     */
    private String code;
    /*跨服务场景，msg需要透传*/
    private String msg;
    /**
     * 异常参数
     */
    private Object[] args;

    public BusinessException(String code) {
        this.code = code;
    }

    public BusinessException(String code, Object[] args) {
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}