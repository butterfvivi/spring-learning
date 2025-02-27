package org.vivi.framework.report.simple.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseBean {

    /**
     * 响应编码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应参数
     */
    private Object[] args;

    private Object ext;

    /**
     * 响应数据
     */
    private Object data;

    public static <T> ResponseBean success() {
        return restResult(null, 200, null);
    }

    public static <T> ResponseBean success(T data) {
        return restResult(data, 200, null);
    }

    public static <T> ResponseBean success(T data, String msg) {
        return restResult(data, 200, msg);
    }

    public static <T> ResponseBean failed() {
        return restResult(null, 500, null);
    }

    public static <T> ResponseBean failed(String msg) {
        return restResult(null, 500, msg);
    }

    public static <T> ResponseBean failed(T data) {
        return restResult(data, 500, null);
    }

    public static <T> ResponseBean failed(T data, String msg) {
        return restResult(data, 500, msg);
    }

    public static <T> ResponseBean failed( int code, String msg) {
        return restResult(null, code, msg);
    }

    public static <T> ResponseBean restResult(T data, int code, String msg) {
        return ResponseBean.builder()
                .code(code)
                .data(data)
                .message(msg)
                .build();

    }
}
