package org.vivi.framework.ireport.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISqlMapper extends BaseMapper {

    List<Map<String, Object>> selectList(String querySql);

    Integer getCount(String countSql);
}
