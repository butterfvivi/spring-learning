package org.vivi.framework.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.quartz.mapper.JobMapper;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> {
}
