package org.vivi.framework.ureport.simple.ureport.definition.dataset;

import java.util.List;

public class ApiResult {

	private String code;
	
	private String message;
	
	private List<?> data;

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

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
}
