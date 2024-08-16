package org.vivi.framework.factory.strategy.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ExportEnum {

    EXPORT_A("DailyAccess"),
    EXPORT_B("DailyGoods"),
    EXPORT_C("DailyUser"),;

    private String exportCode;

    public static List<String> getList() {
        return Arrays.asList(ExportEnum.values())
                .stream()
                .map(ExportEnum::getExportCode)
                .collect(Collectors.toList());
    }

    ExportEnum(String exportCode) {
        this.exportCode = exportCode;
    }

    public String getExportCode() {
        return exportCode;
    }
}
