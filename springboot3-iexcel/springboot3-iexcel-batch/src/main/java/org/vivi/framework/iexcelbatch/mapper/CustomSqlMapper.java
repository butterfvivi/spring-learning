package org.vivi.framework.iexcelbatch.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.vivi.framework.iexcelbatch.entity.dto.DynamicSqlDTO;

@Mapper
public interface CustomSqlMapper {

    /**
     * @describe 执行动态批量插入语句
     * @Param dynamicSql
     */
    int executeCustomSql(@Param("dto") DynamicSqlDTO dto);

    /**
     * @describe 快速清空表
     * @Param tableName
     */
    void truncateTable(@Param("tableName") String tableName);
}
