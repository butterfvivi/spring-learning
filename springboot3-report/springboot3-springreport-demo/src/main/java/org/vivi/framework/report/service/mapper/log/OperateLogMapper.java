package org.vivi.framework.report.service.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.service.model.operatelog.OperateLog;

@Mapper
public interface OperateLogMapper  extends BaseMapper<OperateLog> {
}
