package org.vivi.spring3.quartz.service;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    public void addJob(String jobName,String jobGroup,String triggerName,String triggerGroup,String jobClass,String cron) throws Exception {
        Class<? extends Job> aClass = (Class<? extends Job>) Class.forName("org.spring.quartz.demo" + jobClass);
        JobDetail jobDetail =
                JobBuilder.newJob(aClass).withIdentity(jobName, jobGroup).build();
    }
}
