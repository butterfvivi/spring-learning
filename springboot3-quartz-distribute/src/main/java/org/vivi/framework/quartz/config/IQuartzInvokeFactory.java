package org.vivi.framework.quartz.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.event.IJobEvent;

@Slf4j
@Aspect
@Service
@AllArgsConstructor
public class IQuartzInvokeFactory {

    private final ApplicationEventPublisher publisher;

    @SneakyThrows
    void init(IJob job, Trigger trigger){
        publisher.publishEvent(new IJobEvent(job, trigger));
    }
}
