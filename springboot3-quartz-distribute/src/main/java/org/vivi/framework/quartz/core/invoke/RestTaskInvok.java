package org.vivi.framework.quartz.core.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.exception.TaskException;
import org.vivi.framework.quartz.job.ITaskInvok;

/**
 * 定时任务rest反射实现
 */
@Slf4j
@AllArgsConstructor
@Component("restTaskInvok")
public class RestTaskInvok implements ITaskInvok {


    @Override
    public void invokeMethod(IJob job) throws TaskException {
        try {
            HttpRequest request = HttpUtil.createGet(job.getExecutePath());
            request.execute();
        }catch (Exception e){
            log.error("定时任务restTaskInvok异常,执行任务：{}", job.getExecutePath());
            throw new TaskException("定时任务restTaskInvok业务执行失败,任务：" + job.getExecutePath());
        }
    }
}
