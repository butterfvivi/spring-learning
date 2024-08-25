package org.vivi.framework.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.quartz.entity.IJob;

@Mapper
public interface JobMapper extends BaseMapper<IJob> {
}
