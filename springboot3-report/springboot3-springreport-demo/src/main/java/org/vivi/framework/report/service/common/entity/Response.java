package org.vivi.framework.report.service.common.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Response<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int FAIL = 500;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String message;

	@Getter
	@Setter
	private T data;

	public static <T> Response<T> success() {
		return restResult(null, 200, null);
	}

	public static <T> Response<T> success(T data) {
		return restResult(data, 200, null);
	}

	public static <T> Response<T> success(T data, String msg) {
		return restResult(data, 200, msg);
	}

	public static <T> Response<T> failed() {
		return restResult(null, 500, null);
	}

	public static <T> Response<T> failed(String msg) {
		return restResult(null, 500, msg);
	}

	public static <T> Response<T> failed(T data) {
		return restResult(data, 500, null);
	}

	public static <T> Response<T> failed(T data, String msg) {
		return restResult(data, 500, msg);
	}

	public static <T> Response<T> failed(int code, String msg) {
		return restResult(null, code, msg);
	}

	public static <T> Response<T> restResult(T data, int code, String msg) {
		Response<T> apiResult = new Response<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMessage(msg);
		return apiResult;
	}

}
