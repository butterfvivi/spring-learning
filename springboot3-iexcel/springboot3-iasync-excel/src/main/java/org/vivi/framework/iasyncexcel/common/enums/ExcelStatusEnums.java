package org.vivi.framework.iasyncexcel.common.enums;

import lombok.Getter;

@Getter
public enum ExcelStatusEnums {

    SUBMETTED("submitted",1),

    SUCCESS("success",2),

    IN_PROCESS("in_process",4),

    FAILED("failed",3);

    private String value;

    private Integer code;

    ExcelStatusEnums(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

}
