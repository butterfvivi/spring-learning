package org.vivi.framework.quartz.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.vivi.framework.quartz.entity.IJobLog;
import org.vivi.framework.quartz.service.JobLogService;

@Slf4j
@Service
@RequiredArgsConstructor
public class IJobLogListener {

    private final JobLogService jobLogService;

    @Async
    @Order
    @EventListener(IJobLogEvent.class)
    public void saveJobLog(IJobLogEvent event){
        IJobLog jobLog = event.getJobLog();
        jobLogService.save(jobLog);
        log.info("save job log, jobLog: {}", jobLog);
    }
}
