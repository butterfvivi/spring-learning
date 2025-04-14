package org.vivi.framework.report.bigdata.common.exception;

import lombok.Getter;

public enum ErrorCode {
    VALID_ERROR("00001","校验异常"),
    EXPORT_ERROR("00002","导入异常"),

    ;
    @Getter
    private String code;
    @Getter
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }}

