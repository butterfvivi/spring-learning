package org.vivi.framework.excel.configure.mybatis.mapper;

import java.util.List;
import java.util.Map;

public interface ISqlMapper {

    List<Map<String, Object>> selectList(String querySql);

    Integer getCount(String countSql);
}
