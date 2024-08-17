package org.vivi.framework.iasyncexcel.starter.context.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;

@Mapper
public interface ExcelTaskMapper extends BaseMapper<ExcelTask> {
}
