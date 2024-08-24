package org.vivi.framework.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.quartz.entity.IJobLog;

@Mapper
public interface JobLogMapper extends BaseMapper<IJobLog> {
}
