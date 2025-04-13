package org.vivi.framework.ireport.demo.common.exception;


public class ExcelException extends RuntimeException {

	public ExcelException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelException(String message) {
		super(message, null);
	}

}
