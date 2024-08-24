package org.vivi.framework.quartz.core.invoke;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vivi.framework.quartz.constants.QuartzEnum;
import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.exception.TaskException;
import org.vivi.framework.quartz.job.ITaskInvok;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 定时任务java class反射实现
 */
@Component("javaClassTaskInvok")
@Slf4j
public class JavaClassTaskInvok implements ITaskInvok {

    @Override
    public void invokeMethod(IJob job) throws TaskException {
        Object obj;
        Class clazz;
        Method method;
        Object returnValue;
        try {
            if (StrUtil.isNotBlank(job.getMethodName())) {
                clazz = Class.forName(job.getClassName());
                obj = clazz.newInstance();
                method = clazz.getDeclaredMethod(job.getMethodName(),String.class);
                returnValue = method.invoke(obj,job.getMethodParamsValue());
            }
            else {
                clazz = Class.forName(job.getClassName());
                obj = clazz.newInstance();
                method = clazz.getDeclaredMethod(job.getMethodName());
                returnValue = method.invoke(obj);
            }
            if (StrUtil.isEmpty(returnValue.toString())
                    || QuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())){
                log.error("定时任务javaClassTaskInvok异常,执行任务：{}", job.getClassName());
                throw new TaskException("定时任务javaClassTaskInvok业务执行失败,任务：" + job.getClassName());
            }
        }catch (ClassNotFoundException e) {
            log.error("定时任务java反射类没有找到,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务java反射类没有找到,执行任务：" + job.getClassName());
        }
        catch (IllegalAccessException e) {
            log.error("定时任务java反射类异常,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务java反射类异常,执行任务：" + job.getClassName());
        }
        catch (InstantiationException e) {
            log.error("定时任务java反射类异常,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务java反射类异常,执行任务：" + job.getClassName());
        }
        catch (NoSuchMethodException e) {
            log.error("定时任务java反射执行方法名异常,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务java反射执行方法名异常,执行任务：" + job.getClassName());
        }
        catch (InvocationTargetException e) {
            log.error("定时任务java反射执行异常,执行任务：{}", job.getClassName());
            throw new TaskException("定时任务java反射执行异常,执行任务：" + job.getClassName());
        }
    }
}
