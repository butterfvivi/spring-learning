package org.vivi.framework.easyexcelsimple.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    /**
     * 下载文件失败
     */
    FAILED_DOWNLOAD_FILE("50001", "下载文件失败"),

    FILE_EMPTY("50002", "文件为空");

    /**
     * 响应编码
     */
    private final String code;

    /**
     * 响应信息
     */
    private final String message;

}
