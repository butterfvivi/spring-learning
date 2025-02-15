package org.vivi.framework.generator.ddl.translate.entity;

import lombok.Data;

import java.util.List;

@Data
public class DDLContext {

    String sheetName;
    String tableEnglishName;
    String tableChineseName;
    String databaseType;
    List<RangeContext> dto;

    /**
     *数据库schema名
     */
    private String databaseName;
}
