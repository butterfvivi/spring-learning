package org.vivi.framework.iexcelbatch.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @describe 生成批量插入sqlDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DynamicSqlDTO {

    //表名
    private String tableName;

    //列名集合
    private List<String> columnList;

    //value集合
    private List<List<Object>> valueList;
}