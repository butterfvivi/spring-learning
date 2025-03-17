package org.vivi.framework.codegen.demo.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.vivi.framework.codegen.demo.pojo.ColumnDetail;

import java.util.List;

/**
 * @apiNote 代码生成器Mapper接口层
 */
@Mapper
public interface CodeGeneratorMapper {

    /**
     * 获取数据库中所有表信息
     *
     * @param database 数据库名
     * @return List<ColumnDetail>
     */
    public List<ColumnDetail> getColumnDetailMapVo(@Param("database") String database);

    /**
     * 获取表中所有字段信息
     *
     * @param database  数据库名
     * @param tableName 表名
     * @return List<ColumnDetail>
     */
    public List<ColumnDetail> getColumnDetailMapVoByTableName(@Param("database") String database, @Param("tableName") String tableName);

}
