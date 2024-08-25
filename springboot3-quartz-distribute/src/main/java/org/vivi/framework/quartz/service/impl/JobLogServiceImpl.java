package org.vivi.framework.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.quartz.entity.IJobLog;
import org.vivi.framework.quartz.mapper.JobLogMapper;
import org.vivi.framework.quartz.service.JobLogService;

@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, IJobLog> implements JobLogService {

}
