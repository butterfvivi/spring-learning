package org.vivi.framework.generator.ddl.translate.generator;

import org.vivi.framework.generator.ddl.translate.entity.DDLContext;
import org.vivi.framework.generator.ddl.translate.entity.FieldMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class DatabaseDDLGenerator {

    public static List<FieldMapping> fieldMappings = Arrays.asList(
            new FieldMapping("int", "integer", "number", "INT"),
            new FieldMapping("bigint", "bigint", "number", "BIGINT"),
            new FieldMapping("long", "bigint", "number", "BIGINT"),
            new FieldMapping("varchar", "varchar", "varchar2", "VARCHAR"),
            new FieldMapping("char", "character", "char", "VARCHAR"),
            new FieldMapping("text", "text", "clob", "CLOB"),
            new FieldMapping("boolean", "boolean", "number", "INT"),
            new FieldMapping("date", "date", "date", "TIMESTAMP"),
            new FieldMapping("time", "time", "timestamp", "TIMESTAMP"),
            new FieldMapping("datetime", "timestamp", "timestamp", "TIMESTAMP"),
            new FieldMapping("decimal", "numeric", "number", "NUMERIC"),
            new FieldMapping("double", "double precision", "binary_double", "DOUBLE")
    );
    public static HashMap<String,  DatabaseDDLGenerator> databaseType = new HashMap<String, DatabaseDDLGenerator>() {{
        put("mysql", new MysqlDDLGenerator());
        put("oracle", new OracleDDLGenerator());
    }};

    public abstract String generateDDL(DDLContext ddlContext);
}
