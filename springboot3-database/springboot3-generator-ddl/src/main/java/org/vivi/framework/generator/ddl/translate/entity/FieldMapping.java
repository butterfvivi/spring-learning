package org.vivi.framework.generator.ddl.translate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldMapping {
    String mysqlType;
    String pgType;
    String oracleType;
    String dmType;
}
