package org.vivi.framework.minio.common.response;

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
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int FAIL = 500;

	public static final int SUCCESS = 200;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String message;

	@Getter
	@Setter
	private T data;

	public static <T> R<T> success() {
		return restResult(null, SUCCESS, null);
	}

	public static <T> R<T> success(T data) {
		return restResult(data, SUCCESS, null);
	}

	public static <T> R<T> success(T data, String msg) {
		return restResult(data, SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, FAIL, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, FAIL, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, FAIL, msg);
	}

	public static <T> R<T> failed(int code, String msg) {
		return restResult(null, code, msg);
	}

	public static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMessage(msg);
		return apiResult;
	}

}
