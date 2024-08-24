package org.vivi.framework.quartz.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("qrtz_job_log")
@Schema(description = "定时任务日志")
public class IJobLog {

    /**
     * 任务日志ID
     */
    @TableId(value = "job_log_id", type = IdType.ASSIGN_ID)
    private Long jobLogId;

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 组内执行顺利，值越大执行优先级越高，最大值9，最小值1
     */
    private String jobOrder;

    /**
     * 1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他
     */
    private String jobType;

    /**
     * job_type=3时，rest调用地址，仅支持post协议;job_type=4时，jar路径;其它值为空
     */
    private String executePath;

    /**
     * job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空
     */
    private String className;

    /**
     * 任务方法
     */
    private String methodName;

    /**
     * 参数值
     */
    private String methodParamsValue;

    /**
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * 日志信息
     */
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    private String jobLogStatus;

    /**
     * 执行时间
     */
    private String executeTime;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
