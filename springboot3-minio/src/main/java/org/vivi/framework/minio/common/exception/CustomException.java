package org.vivi.framework.minio.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

    /**
     * 响应编码
     */
    private String code;

    /**
     * 响应信息
     */
    private String message;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public CustomException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}