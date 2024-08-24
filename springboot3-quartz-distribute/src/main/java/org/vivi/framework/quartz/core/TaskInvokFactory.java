package org.vivi.framework.quartz.core;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.quartz.config.SpringContextHolder;
import org.vivi.framework.quartz.constants.JobTypeQuartzEnum;
import org.vivi.framework.quartz.exception.TaskException;
import org.vivi.framework.quartz.job.ITaskInvok;

@Slf4j
public class TaskInvokFactory {

    public static ITaskInvok getInvoke(String jobType) throws TaskException {
        if (StrUtil.isBlank(jobType)){
            log.info("获取TaskInvok传递参数有误，jobType:{}", jobType);
            throw new TaskException("");
        }
        ITaskInvok iTaskInvok = null;

        if (JobTypeQuartzEnum.JAVA.getType().equals(jobType)){
            iTaskInvok = SpringContextHolder.getBean("javaClassTaskInvok");
        }
        else if (JobTypeQuartzEnum.SPRING_BEAN.getType().equals(jobType)){
            iTaskInvok = SpringContextHolder.getBean("springBeanTaskInvok");
        }
        else if (JobTypeQuartzEnum.REST.getType().equals(jobType)){
            iTaskInvok = SpringContextHolder.getBean("restTaskInvok");
        }
        else if (JobTypeQuartzEnum.JAR.getType().equals(jobType)){
            iTaskInvok = SpringContextHolder.getBean("jarTaskInvok");
        }
        else if (StrUtil.isBlank(jobType)){
            log.info("定时任务类型无对应反射方式，反射类型:{}", jobType);
            throw new TaskException("");
        }
        return iTaskInvok;
    }
}
