package org.vivi.framework.report.simple.common.exception;

public class BusinessExceptionBuilder {

    /**
     * 构建code
     * @param code 异常码
     * @return
     */
    public static BusinessException build(String code){
        return new BusinessException(code);
    }

    /**
     * 构建code和args
     * @param code 异常码
     * @param args 异常参数
     * @return
     */
    public static BusinessException build(String code ,Object... args){
        return new BusinessException(code, args);
    }
    public static BusinessException with(String code ,String msg){
        BusinessException ex = new BusinessException(code);
        ex.setMsg(msg);
        return ex;
    }
}
