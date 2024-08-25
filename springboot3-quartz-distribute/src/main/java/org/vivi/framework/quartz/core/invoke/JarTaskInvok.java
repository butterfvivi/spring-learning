package org.vivi.framework.quartz.core.invoke;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.exception.TaskException;
import org.vivi.framework.quartz.job.ITaskInvok;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("jarTaskInvok")
public class JarTaskInvok implements ITaskInvok {

    @Override
    public void invokeMethod(IJob job) throws TaskException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        File jar = new File(job.getExecutePath());
        processBuilder.directory(jar.getParentFile());
        List<String> commands = new ArrayList<>();
        commands.add("java");
        commands.add("-jar");
        commands.add(job.getExecutePath());

        if (StrUtil.isNotEmpty(job.getMethodParamsValue())){
            commands.add(job.getMethodParamsValue());
        }
        processBuilder.command(commands);
        try {
            processBuilder.start();
        }catch (IOException e){
            log.error("定时任务jar反射执行异常,执行任务：{}", job.getExecutePath());
            throw new TaskException("定时任务jar反射执行异常,执行任务：" + job.getExecutePath());
        }
    }
}
