package org.vivi.framework.report.service.handler.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IOperateLogService;
import org.vivi.framework.report.service.mapper.log.OperateLogMapper;
import org.vivi.framework.report.service.model.operatelog.OperateLog;

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements IOperateLogService {
}
