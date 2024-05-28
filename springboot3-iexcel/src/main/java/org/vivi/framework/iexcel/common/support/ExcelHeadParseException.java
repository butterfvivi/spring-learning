package org.vivi.framework.iexcel.common.support;


public class ExcelHeadParseException extends RuntimeException{

    public ExcelHeadParseException(String message) {
        super(message);
    }

    public ExcelHeadParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelHeadParseException(Throwable cause) {
        super(cause);
    }
}
