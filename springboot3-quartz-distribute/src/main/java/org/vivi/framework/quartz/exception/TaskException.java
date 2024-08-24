package org.vivi.framework.quartz.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskException extends RuntimeException {

    /**
     * 响应编码
     */
    private String code;

    /**
     * 响应信息
     */
    private String message;

    public TaskException(String message) {
        this.message = message;
    }

    public TaskException(Throwable cause) {
        super(cause);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public TaskException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}