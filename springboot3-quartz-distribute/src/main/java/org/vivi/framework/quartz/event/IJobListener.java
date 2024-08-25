package org.vivi.framework.quartz.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.utils.TaskInvokUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class IJobListener {

    private final TaskInvokUtil taskInvokUtil;

    @Async
    @Order
    @EventListener(IJobEvent.class)
    public void comJob(IJobEvent event){
        IJob job = event.getJob();
        Trigger trigger = event.getTrigger();
        taskInvokUtil.invokeMethod(job,trigger);
    }

}
