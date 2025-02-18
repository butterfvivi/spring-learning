package org.vivi.framework.report.simple.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseBean {

    /**
     * 响应编码
     */
    private String code;

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
}
