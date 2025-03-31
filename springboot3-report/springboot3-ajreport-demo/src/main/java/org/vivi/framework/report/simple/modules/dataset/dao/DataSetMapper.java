package org.vivi.framework.report.simple.modules.dataset.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.simple.modules.dataset.entity.DataSet;

@Mapper
public interface DataSetMapper extends BaseMapper<DataSet> {

}
