package org.vivi.framework.atabase.lock.common;

/**
* 自定义业务异常
*
*/
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = -7295514184284150311L;

	private String code;
	
	private String message;
	
	public ServiceException(String code, String message){
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
