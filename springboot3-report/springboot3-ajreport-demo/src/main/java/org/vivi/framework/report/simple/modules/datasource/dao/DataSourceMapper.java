package org.vivi.framework.report.simple.modules.datasource.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.simple.modules.datasource.entity.DataSource;


@Mapper
public interface DataSourceMapper extends BaseMapper<DataSource> {

}
