package org.vivi.framework.quartz.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.vivi.framework.quartz.config.IQuartzFactory;
import org.vivi.framework.quartz.constants.QuartzEnum;
import org.vivi.framework.quartz.entity.IJob;

/**
 * 定时任务的工具类
 */
@Slf4j
@Component
public class TaskUtil {

    /**
     * 获取定时任务的唯一key
     */
    public static JobKey getJobKey(IJob job){
        return JobKey.jobKey(job.getJobName(),job.getJobGroup());
    }

    /**
     * 获取定时任务触发器cron的唯一key
     */
    public static TriggerKey getTriggerKey(IJob job){
        return TriggerKey.triggerKey(job.getJobName(),job.getJobGroup());
    }

    /**
     * 添加或者更新定时任务
     * @param job
     * @param scheduler
     */
    public void addOrUpdateJob(IJob job, Scheduler scheduler){
        CronTrigger trigger = null;
        JobKey jobKey = getJobKey(job);
        //获得触发器
        TriggerKey triggerKey = getTriggerKey(job);
        try {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 判断触发器是否存在（如果存在说明之前运行过但是在当前被禁用了，如果不存在说明一次都没运行过）
            if (trigger == null){
                // 新建一个工作任务 指定任务类型为串接进行的
                JobDetail jobDetail = JobBuilder.newJob(IQuartzFactory.class).withIdentity(jobKey).build();
                jobDetail.getJobDataMap().put(QuartzEnum.SCHEDULE_JOB_KEY.getType(), job);
                // 将cron表达式进行转换
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                cronScheduleBuilder = this.handleCronSchedulerMisfirePolicy(cronScheduleBuilder,job);
                //创建触发器并将cron表达式对象给塞入
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(cronScheduleBuilder)
                        .build();
                //在调度器中将触发器和任务进行组合
                scheduler.scheduleJob(jobDetail,trigger);
            }
            else {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                cronScheduleBuilder = this.handleCronSchedulerMisfirePolicy(cronScheduleBuilder,job);
                //按照新的规则进行
                trigger.getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .withSchedule(cronScheduleBuilder)
                        .build();
                //将任务信息更新到任务信息中
                trigger.getJobDataMap().put(QuartzEnum.SCHEDULE_JOB_KEY.getType(), job);
                scheduler.rescheduleJob(triggerKey,trigger);
            }
            //如果任务状态是暂停
            if (job.getJobStatus().equals(QuartzEnum.JOB_STATUS_NOT_RUNNING.getType())){
                this.pauseJob(job,scheduler);
            }
        }
        catch (SchedulerException e){
            log.error("添加或更新定时任务，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 立即执行一次任务
     * @param scheduler
     * @param job
     * @return
     */
    public static boolean runOnce(Scheduler scheduler , IJob job){
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(QuartzEnum.SCHEDULE_JOB_KEY.getType(), job);
            scheduler.triggerJob(getJobKey(job), dataMap);
        }catch (SchedulerException e){
            log.error("立刻执行定时任务，失败信息：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 暂停定时任务
     * @param job
     * @param scheduler
     */
    public void pauseJob(IJob job , Scheduler scheduler){
        try {
            if (scheduler != null) {
                scheduler.pauseJob(getJobKey(job));
            }
        }catch (SchedulerException e){
            log.error("暂停定时任务，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 恢复定时任务
     * @param job
     * @param scheduler
     */
    public void resumeJob(IJob job , Scheduler scheduler){
        try {
            if (scheduler != null) {
                scheduler.resumeJob(getJobKey(job));
            }
        }catch (SchedulerException e){
            log.error("恢复定时任务，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 移除定时任务
     * @param job
     * @param scheduler
     */
    public void removeJob(IJob job , Scheduler scheduler){
        try {
            if (scheduler != null){
                // 停止触发器
                scheduler.pauseTrigger(getTriggerKey(job));
                // 移除触发器
                scheduler.unscheduleJob(getTriggerKey(job));
                // 删除任务
                scheduler.deleteJob(getJobKey(job));
            }
        }catch (Exception e){
            log.error("删除定时任务，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 启动所有运行定时任务
     * @param scheduler
     */
    public void startJobs(Scheduler scheduler){
        try {
            if (scheduler != null){
                scheduler.start();
            }
        }catch (Exception e){
            log.error("启动定时任务，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 暂停所有定时任务
     * @param scheduler
     */
    public void pauseJobs(Scheduler scheduler){
        try{
            if (scheduler != null){
                scheduler.pauseAll();
            }
        }catch (Exception e){
            log.error("暂停所有定时任务，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 获取错失执行策略方法
     * @param cronScheduleBuilder
     * @param job
     * @return
     */
    public CronScheduleBuilder handleCronSchedulerMisfirePolicy(CronScheduleBuilder cronScheduleBuilder, IJob job) {
        if (QuartzEnum.MISFIRE_DEFAULT.getType().equals(job.getMisfirePolicy())){
            return cronScheduleBuilder;
        }
        else if (QuartzEnum.MISFIRE_IGNORE_MISFIRES.getType().equals(job.getMisfirePolicy())){
            return cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        }
        else if (QuartzEnum.MISFIRE_FIRE_AND_PROCEED.getType().equals(job.getMisfirePolicy())){
            return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        }
        else if (QuartzEnum.MISFIRE_DO_NOTHING.getType().equals(job.getMisfirePolicy())){
            return cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        }else {
            return cronScheduleBuilder;
        }
    }

    /**
     * 判断cron表达式是否正确
     * @param cronExpression
     * @return
     */
    public boolean isValidCron(String cronExpression){
        return CronExpression.isValidExpression(cronExpression);
    }
}
