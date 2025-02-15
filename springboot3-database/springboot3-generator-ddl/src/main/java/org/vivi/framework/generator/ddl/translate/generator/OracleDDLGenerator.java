package org.vivi.framework.generator.ddl.translate.generator;

import cn.hutool.core.util.StrUtil;
import org.vivi.framework.generator.ddl.translate.entity.DDLContext;
import org.vivi.framework.generator.ddl.translate.entity.FieldMapping;
import org.vivi.framework.generator.ddl.translate.entity.RangeContext;
import org.vivi.framework.generator.ddl.utils.JlStrUtil;

import java.util.List;
import java.util.stream.Collectors;

public class OracleDDLGenerator extends DatabaseDDLGenerator{

    @Override
    public String generateDDL(DDLContext ddlContext) {
        String sheetName = ddlContext.getSheetName();
        String tableEnglishName = ddlContext.getTableEnglishName();
        List<RangeContext> dto = ddlContext.getDto();

        String lineSeparator = System.lineSeparator();

        System.out.println("-----------开始解析oracle语法---------------当前读取的sheet页---"+sheetName+"-----------------------");
        String createTable = "create table " + tableEnglishName + " (" + lineSeparator;

        StringBuilder ddl = new StringBuilder(createTable);

        List<String> fieldsDDL = dto.stream()
                .map(field -> {
                    String fieldName = field.getFieldName();
                    String fieldType = field.getFieldType();
                    String fieldLength = field.getFieldLength();
                    String isPrimaryKey = field.getIsPrimaryKey();
                    String isSelfIncreasing = field.getIsSelfIncreasing();
                    String fieldComment = field.getFieldComment();
                    String fieldCommentAppend = field.getFieldCommentAppend();

                    // 根据字段类型映射关系映射为 PostgreSQL DDL
                    String pgFieldType = mapFieldType(fieldType);

                    if (pgFieldType == null) {
                        // 如果映射关系中未定义，使用默认类型
                        pgFieldType = "text";
                    }

                    String fieldDDL = "\"" + fieldName + "\" " + pgFieldType;

                    if (!"0".equals(fieldLength)) {
                        fieldDDL += "(" + fieldLength + ")";
                    }

                    if ("Y".equals(isPrimaryKey)) {
                        fieldDDL += " primary key";
                    }

                    if ("Y".equals(isSelfIncreasing)) {
                        fieldDDL += " serial";
                    }

                    return fieldDDL;
                })
                .collect(Collectors.toList());

        ddl.append(String.join("," + lineSeparator, fieldsDDL));

        ddl.append(");" + lineSeparator);
        //todo 添加表注释
        String tableComment = ddlContext.getTableChineseName(); // 获取表注释
        if (StrUtil.isNotEmpty(tableComment)) {
            ddl.append("comment on table " + tableEnglishName + " is '" + tableComment + "';" + lineSeparator);
        }

        //todo 添加字段注释
        dto.stream()
                .filter(rangeContext -> StrUtil.isNotEmpty(rangeContext.getFieldComment()))
                .forEach(rangeContext -> {
                    String fieldName = rangeContext.getFieldName();
                    fieldName = JlStrUtil.makeSurroundWith(fieldName, "\"");
                    String fieldComment = rangeContext.getFieldComment(); // 获取字段注释
                    ddl.append("comment on column " + tableEnglishName + "." + fieldName + " is '" + fieldComment + "';" + lineSeparator);
                });

        ddl.append("-----------------------------------------------------------------------------------" + lineSeparator);

        String string = ddl.toString();
        return string;
    }

    private String mapFieldType(String fieldType) {
        for (FieldMapping mapping : fieldMappings) {
            if (mapping.getMysqlType().equalsIgnoreCase(fieldType)) {
                return mapping.getOracleType();
            }
        }
        return null; // 映射关系未定义时返回 null
    }

}
