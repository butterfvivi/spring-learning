package org.vivi.framework.quartz.core.invoke;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.vivi.framework.quartz.config.SpringContextHolder;
import org.vivi.framework.quartz.constants.QuartzEnum;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.exception.TaskException;
import org.vivi.framework.quartz.job.ITaskInvok;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 定时任务spring bean反射实现
 */
@Slf4j
@Component("springBeanTaskInvok")
public class SpringBeanTaskInvok implements ITaskInvok {


    @Override
    public void invokeMethod(IJob job) throws TaskException {
        Object target;
        Method method;
        Object returnValue;
        //通过spring上下文去找，也有可能找不到
        target = SpringContextHolder.getBean(job.getClassName());
        try {
            if (StrUtil.isNotBlank(job.getMethodParamsValue())) {
                method = target.getClass().getDeclaredMethod(job.getMethodName(), String.class);
                ReflectionUtils.makeAccessible(method);
                returnValue = method.invoke(target ,job.getMethodParamsValue());
            }
            else {
                method = target.getClass().getDeclaredMethod(job.getMethodName());
                ReflectionUtils.makeAccessible(method);
                returnValue = method.invoke(target);
            }if (StrUtil.isEmpty(returnValue.toString())
            || QuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())){
                log.error("定时任务spring bean反射执行异常,执行任务：{}", job.getClassName());
                throw new TaskException("定时任务spring bean反射执行异常,执行任务：" + job.getClassName());
            }
        }
        catch (NoSuchMethodException e) {
            log.error("定时任务spring bean反射异常方法未找到,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务spring bean反射异常方法未找到,执行任务：" + job.getClassName());
        }
        catch (IllegalAccessException e) {
            log.error("定时任务spring bean反射异常,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务spring bean反射异常,执行任务：" + job.getClassName());
        }
        catch (InvocationTargetException e) {
            log.error("定时任务spring bean反射执行异常,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务spring bean反射执行异常,执行任务：" + job.getClassName());
        }
    }
}
