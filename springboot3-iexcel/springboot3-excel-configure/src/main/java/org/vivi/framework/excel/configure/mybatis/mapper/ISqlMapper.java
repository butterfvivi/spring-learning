package org.vivi.framework.excel.configure.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISqlMapper {

    List<Map<String, Object>> selectList(String querySql);

    Integer getCount(String countSql);
}
