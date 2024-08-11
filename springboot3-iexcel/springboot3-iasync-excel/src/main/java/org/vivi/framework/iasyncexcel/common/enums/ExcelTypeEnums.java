package org.vivi.framework.iasyncexcel.common.enums;

import lombok.Getter;

@Getter
public enum ExcelTypeEnums {

    EXPORT(1),IMPORT(2);

    private int code;

    ExcelTypeEnums(int code) {
        this.code = code;
    }

}
