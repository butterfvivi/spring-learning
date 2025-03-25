package org.vivi.framework.chart.simple.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {

    /**
     * 响应编码
     */
    private String code;

    /**
     * 响应信息
     */
    private String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}