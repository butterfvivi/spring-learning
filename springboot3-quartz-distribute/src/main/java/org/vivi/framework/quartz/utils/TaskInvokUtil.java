package org.vivi.framework.quartz.utils;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.vivi.framework.quartz.constants.QuartzEnum;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.entity.IJobLog;
import org.vivi.framework.quartz.event.IJobLogEvent;
import org.vivi.framework.quartz.core.TaskInvokFactory;
import org.vivi.framework.quartz.job.ITaskInvok;
import org.vivi.framework.quartz.service.JobService;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskInvokUtil {

    private final ApplicationEventPublisher publisher;

    private final JobService jobService;

    public void invokeMethod(IJob job , Trigger trigger){
        // 执行开始时间
        long startTime;
        // 执行结束时间
        long endTime;
        // 获取执行开始时间
        startTime = System.currentTimeMillis();
        // 更新定时任务表内的状态、执行时间、上次执行时间、下次执行时间等信息
        IJob uploadJob = new IJob();
        uploadJob.setJobId(job.getJobId());
        // 日志
        IJobLog jobLog = new IJobLog();
        jobLog.setJobId(job.getJobId());
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setJobOrder(job.getJobOrder());
        jobLog.setJobType(job.getJobType());
        jobLog.setExecutePath(job.getExecutePath());
        jobLog.setClassName(job.getClassName());
        jobLog.setMethodName(job.getMethodName());
        jobLog.setMethodParamsValue(job.getMethodParamsValue());
        jobLog.setCronExpression(job.getCronExpression());
        try {
            // 执行任务
            ITaskInvok invoke = TaskInvokFactory.getInvoke(job.getJobType());
            // 确保租户上下文有值，使得当前线程中的多租户特性生效。
            invoke.invokeMethod(job);
            // 记录成功状态
            jobLog.setJobMessage(QuartzEnum.JOB_LOG_STATUS_SUCCESS.getDescription());
            jobLog.setJobLogStatus(QuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
            // 任务表信息更新
            uploadJob.setJobExecuteStatus(QuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
        }catch (Throwable e){
            log.error("定时任务执行失败，任务名称：{}；任务组名：{}，cron执行表达式：{}，执行时间：{}", job.getJobName(), job.getJobGroup(),
                    job.getCronExpression(), new Date());
            // 记录失败状态
            jobLog.setJobMessage(QuartzEnum.JOB_LOG_STATUS_FAIL.getDescription());
            jobLog.setJobLogStatus(QuartzEnum.JOB_LOG_STATUS_FAIL.getType());
            jobLog.setExceptionInfo(StrUtil.sub(e.getMessage(), 0, 2000));
            // 任务表信息更新
            uploadJob.setJobExecuteStatus(QuartzEnum.JOB_LOG_STATUS_FAIL.getType());
        }finally {
            // 记录执行时间 立刻执行使用的是simpleTeigger
            if (trigger instanceof CronTrigger) {
                uploadJob
                        .setStartTime(trigger.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                uploadJob.setPreviousTime(
                        trigger.getPreviousFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                uploadJob.setNextTime(
                        trigger.getNextFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            // 记录执行时长
            endTime = System.currentTimeMillis();
            jobLog.setExecuteTime(String.valueOf(endTime - startTime));

            publisher.publishEvent(new IJobLogEvent(jobLog));
            jobService.updateById(uploadJob);
        }
    }
}
