package org.vivi.framework.securityjwt.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException{

    public BaseException(Integer responseCode, String message) {
        super(message);
    }


}
