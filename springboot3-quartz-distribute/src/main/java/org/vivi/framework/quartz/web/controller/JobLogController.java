package org.vivi.framework.quartz.web.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.quartz.entity.IJobLog;
import org.vivi.framework.quartz.service.JobLogService;
import org.vivi.framework.quartz.web.response.R;

@RestController
@AllArgsConstructor
@RequestMapping("/job-log")
@Tag(description = "job-log", name = "定时任务日志")
public class JobLogController {

    private final JobLogService jobLogService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysJobLog 定时任务执行日志表
     * @return
     */
    @GetMapping("/page")
    @Operation(description = "分页定时任务日志查询")
    public R getSysJobLogPage(Page page, IJobLog sysJobLog) {
        return R.success(jobLogService.page(page, Wrappers.query(sysJobLog)));
    }

    @DeleteMapping
    @Operation(description = "批量删除日志")
    public R deleteLogs(@RequestBody Long[] ids) {
        return R.success(jobLogService.removeBatchByIds(CollUtil.toList(ids)));
    }
}
