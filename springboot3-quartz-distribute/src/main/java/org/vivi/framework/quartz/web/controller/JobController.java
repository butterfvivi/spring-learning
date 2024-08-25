package org.vivi.framework.quartz.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.quartz.constants.QuartzEnum;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.service.JobService;
import org.vivi.framework.quartz.utils.TaskUtil;
import org.vivi.framework.quartz.web.response.R;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/job")
@Tag(description = "Job",name = "Job")
//@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class JobController {

    @Autowired
    private JobService jobService;

    private final TaskUtil taskUtil;

    private final Scheduler scheduler;
    /**
     * 定时任务分页查询
     * @param page 分页对象
     * @param job 定时任务调度表
     * @return
     */
    @GetMapping("/page")
    @Operation(description = "分页定时业务查询")
    public R getSysJobPage(Page page, IJob job) {
        LambdaQueryWrapper<IJob> wrapper = Wrappers.<IJob>lambdaQuery()
                .like(StrUtil.isNotBlank(job.getJobName()), IJob::getJobName, job.getJobName())
                .like(StrUtil.isNotBlank(job.getJobGroup()), IJob::getJobGroup, job.getJobGroup())
                .eq(StrUtil.isNotBlank(job.getJobStatus()), IJob::getJobStatus, job.getJobGroup())
                .eq(StrUtil.isNotBlank(job.getJobExecuteStatus()), IJob::getJobExecuteStatus,
                        job.getJobExecuteStatus());
        return R.success(jobService.page(page, wrapper));
    }

    /**
     * 通过id查询定时任务
     * @param id id
     * @return R
     */
    @GetMapping("/{id}")
    @Operation(description = "唯一标识查询定时任务")
    public R getById(@PathVariable("id") Long id) {
        return R.success(jobService.getById(id));
    }

    /**
     * 新增定时任务
     * @param job 定时任务调度表
     * @return R
     */
    @PostMapping
    @Operation(description = "新增定时任务")
    public R save(@RequestBody IJob job) {
        job.setJobStatus(QuartzEnum.JOB_STATUS_RELEASE.getType());
        //job.setCreateBy(SecurityUtils.getUser().getUsername());
        jobService.save(job);
        // 初始化任务
        taskUtil.addOrUpdateJob(job, scheduler);
        return R.success();
    }

    /**
     * 修改定时任务
     * @param job 定时任务调度表
     * @return R
     */
    @PutMapping
    @Operation(description = "修改定时任务")
    public R updateById(@RequestBody IJob job) {
        //job.setUpdateBy(SecurityUtils.getUser().getUsername());
        IJob querySysJob = this.jobService.getById(job.getJobId());
        if (QuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(querySysJob.getJobStatus())) {
            this.taskUtil.addOrUpdateJob(job, scheduler);
            jobService.updateById(job);
        }
        else if (QuartzEnum.JOB_STATUS_RELEASE.getType().equals(querySysJob.getJobStatus())) {
            jobService.updateById(job);
        }
        return R.success();
    }

    /**
     * 通过id删除定时任务
     * @param id id
     * @return R
     */
    @DeleteMapping("/{id}")
    @Operation(description = "唯一标识查询定时任务，暂停任务才能删除")
    public R removeById(@PathVariable Long id) {
        IJob querySysJob = this.jobService.getById(id);
        if (QuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(querySysJob.getJobStatus())) {
            this.taskUtil.removeJob(querySysJob, scheduler);
            this.jobService.removeById(id);
        }
        else if (QuartzEnum.JOB_STATUS_RELEASE.getType().equals(querySysJob.getJobStatus())) {
            this.jobService.removeById(id);
        }
        return R.success();
    }

    /**
     * 暂停全部定时任务
     * @return
     */
    @PostMapping("/shutdown-jobs")
    @Operation(description = "暂停全部定时任务")
    public R shutdownJobs() {
        taskUtil.pauseJobs(scheduler);
        long count = this.jobService.count(
                new LambdaQueryWrapper<IJob>().eq(IJob::getJobStatus, QuartzEnum.JOB_STATUS_RUNNING.getType()));
        if (count <= 0) {
            return R.success("无正在运行定时任务");
        }
        else {
            // 更新定时任务状态条件，运行状态2更新为暂停状态2
            this.jobService.update(
                    IJob.builder().jobStatus(QuartzEnum.JOB_STATUS_NOT_RUNNING.getType()).build(),
                    new UpdateWrapper<IJob>().lambda()
                            .eq(IJob::getJobStatus, QuartzEnum.JOB_STATUS_RUNNING.getType()));
            return R.success("暂停成功");
        }
    }

    /**
     * 启动全部定时任务
     * @return
     */
    @PostMapping("/start-jobs")
    @Operation(description = "启动全部定时任务")
    public R startJobs() {
        // 更新定时任务状态条件，暂停状态3更新为运行状态2
        this.jobService.update(IJob.builder().jobStatus(QuartzEnum.JOB_STATUS_RUNNING.getType()).build(),
                new UpdateWrapper<IJob>().lambda()
                        .eq(IJob::getJobStatus, QuartzEnum.JOB_STATUS_NOT_RUNNING.getType()));
        taskUtil.startJobs(scheduler);
        return R.success();
    }

    /**
     * 刷新全部定时任务
     * @return
     */
    @PostMapping("/refresh-jobs")
    public R refreshJobs() {
        jobService.list().forEach((sysjob) -> {
            if (QuartzEnum.JOB_STATUS_RELEASE.getType().equals(sysjob.getJobStatus())
                    || QuartzEnum.JOB_STATUS_DEL.getType().equals(sysjob.getJobStatus())) {
                taskUtil.removeJob(sysjob, scheduler);
            }
            else if (QuartzEnum.JOB_STATUS_RUNNING.getType().equals(sysjob.getJobStatus())
                    || QuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(sysjob.getJobStatus())) {
                taskUtil.addOrUpdateJob(sysjob, scheduler);
            }
            else {
                taskUtil.removeJob(sysjob, scheduler);
            }
        });
        return R.success();
    }

    /**
     * 启动定时任务
     * @param jobId
     * @return
     */
    @PostMapping("/start-job/{id}")
    @Operation(description = "启动定时任务")
    public R startJob(@PathVariable("id") Long jobId) {
        IJob querySysJob = this.jobService.getById(jobId);
        if (querySysJob != null && QuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(querySysJob.getJobStatus())) {
            taskUtil.addOrUpdateJob(querySysJob, scheduler);
        }
        else {
            taskUtil.resumeJob(querySysJob, scheduler);
        }
        // 更新定时任务状态条件，暂停状态3更新为运行状态2
        this.jobService
                .updateById(IJob.builder().jobId(jobId).jobStatus(QuartzEnum.JOB_STATUS_RUNNING.getType()).build());
        return R.success();
    }

    /**
     * 启动定时任务
     * @param jobId
     * @return
     */
    @PostMapping("/run-job/{id}")
    @Operation(description = "立刻执行定时任务")
    public R runJob(@PathVariable("id") Long jobId) {
        IJob querySysJob = this.jobService.getById(jobId);
        return TaskUtil.runOnce(scheduler, querySysJob) ? R.success() : R.failed();
    }

    /**
     * 暂停定时任务
     * @return
     */
    @PostMapping("/shutdown-job/{id}")
    @Operation(description = "暂停定时任务")
    public R shutdownJob(@PathVariable("id") Long id) {
        IJob querySysJob = this.jobService.getById(id);
        // 更新定时任务状态条件，运行状态2更新为暂停状态3
        this.jobService.updateById(IJob.builder()
                .jobId(querySysJob.getJobId())
                .jobStatus(QuartzEnum.JOB_STATUS_NOT_RUNNING.getType())
                .build());
        taskUtil.pauseJob(querySysJob, scheduler);
        return R.success();
    }

    /**
     * 唯一标识查询定时执行日志
     * @return
     */
    @GetMapping("/job-log")
    @Operation(description = "唯一标识查询定时执行日志")
    public R getJobLog(Page page, IJob sysJobLog) {
        return R.success(jobService.page(page, Wrappers.query(sysJobLog)));
    }

    /**
     * 检验任务名称和任务组联合是否唯一
     * @return
     */
    @GetMapping("/is-valid-task-name")
    @Operation(description = "检验任务名称和任务组联合是否唯一")
    public R isValidTaskName(@RequestParam String jobName, @RequestParam String jobGroup) {
        return this.jobService
                .count(Wrappers.query(IJob.builder().jobName(jobName).jobGroup(jobGroup).build())) > 0
                ? R.failed("任务重复，请检查此组内是否已包含同名任务") : R.success();
    }

    /**
     * 导出任务
     * @param sysJob
     * @return
     */
    @GetMapping("/export")
    @Operation(description = "导出任务")
    public List<IJob> export(IJob sysJob) {
        return jobService.list(Wrappers.query(sysJob));
    }

}
