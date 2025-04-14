package org.vivi.framework.report.bigdata.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 所有异常类的父类
 */
@Data
@NoArgsConstructor // 默认无参构造函数
public class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	protected Object code;
	protected String message;
	protected Object data;

	public BaseRuntimeException(Throwable e) {
		super(e);
	}

	public BaseRuntimeException(Object code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BaseRuntimeException(Object code, String message, Object data) {
		super(message);
		this.code = code;
		this.message = message;
		this.data = data;
	}

}
