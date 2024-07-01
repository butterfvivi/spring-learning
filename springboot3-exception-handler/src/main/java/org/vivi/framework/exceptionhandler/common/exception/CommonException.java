package org.vivi.framework.exceptionhandler.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.vivi.framework.exceptionhandler.common.enums.ResponseCodeEnum;

@Getter
@Setter
public class CommonException  extends RuntimeException {

    /**
     * 响应编码
     */
    private String code;

    /**
     * 响应信息
     */
    private String message;

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CommonException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.code = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getMessage();
    }

    /**
     * 不记录异常栈信息
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}