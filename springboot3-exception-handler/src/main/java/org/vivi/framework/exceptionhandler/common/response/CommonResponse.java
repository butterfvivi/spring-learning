package org.vivi.framework.exceptionhandler.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.vivi.framework.exceptionhandler.common.constant.Constants;
import org.vivi.framework.exceptionhandler.common.enums.ResponseCodeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse <T> implements Serializable {

    private static final long serialVersionUID = -1338376281028943181L;

    @ApiModelProperty(value = "响应编码")
    private String code;

    @ApiModelProperty(value = "响应信息")
    private String message;

    @ApiModelProperty(value = "业务数据")
    private T data;

    @ApiModelProperty(value = "traceId")
    private String traceId = MDC.get(Constants.MDC_KEY);

    @ApiModelProperty(value = "响应日期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime localDateTime = LocalDateTime.now();

    public CommonResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(ResponseCodeEnum ResponseCodeEnum) {
        this.code = ResponseCodeEnum.getCode();
        this.message = ResponseCodeEnum.getMessage();
    }



    public CommonResponse(ResponseCodeEnum ResponseCodeEnum, T data) {
        this.code = ResponseCodeEnum.getCode();
        this.message = ResponseCodeEnum.getMessage();
        this.data = data;
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(ResponseCodeEnum.SUCCESS);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(ResponseCodeEnum.SUCCESS, data);
    }

    public static <T> CommonResponse<T> success(ResponseCodeEnum ResponseCodeEnum, T data) {
        return new CommonResponse<>(ResponseCodeEnum, data);
    }

    public static <T> CommonResponse<T> failure() {
        return new CommonResponse<>(ResponseCodeEnum.SYSTEM_EXCEPTION);
    }

    public static <T> CommonResponse<T> failure(ResponseCodeEnum ResponseCodeEnum) {
        return new CommonResponse<>(ResponseCodeEnum);
    }

}