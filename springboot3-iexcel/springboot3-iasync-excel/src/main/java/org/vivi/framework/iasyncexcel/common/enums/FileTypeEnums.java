package org.vivi.framework.iasyncexcel.common.enums;

public enum FileTypeEnums {

    XLSX("1"),PDF("2"),XLS("3");

    private String code;

    FileTypeEnums(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
