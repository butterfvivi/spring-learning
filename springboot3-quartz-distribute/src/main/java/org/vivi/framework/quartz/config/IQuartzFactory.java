package org.vivi.framework.quartz.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.vivi.framework.quartz.constants.QuartzEnum;
import org.vivi.framework.quartz.entity.IJob;

@Slf4j
@DisallowConcurrentExecution
public class IQuartzFactory implements Job {

    private IQuartzInvokeFactory quartzInvokeFactory;

    public void execute(JobExecutionContext jobExecutionContext){
        IJob job = (IJob) jobExecutionContext.getMergedJobDataMap()
                .get(QuartzEnum.SCHEDULE_JOB_KEY.getType());
        quartzInvokeFactory.init(job, jobExecutionContext.getTrigger());
    }
}
