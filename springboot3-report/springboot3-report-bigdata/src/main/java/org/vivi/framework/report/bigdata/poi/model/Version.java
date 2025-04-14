package org.vivi.framework.report.bigdata.poi.model;

import lombok.Getter;

public enum Version {
    XLS("2003"),
    XLSX("2007"),
    ;
    @Getter
    private String value;

    Version(String value) {
        this.value = value;
    }
}
