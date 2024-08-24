package org.vivi.framework.quartz.job;

import org.vivi.framework.quartz.entity.IJob;
import org.vivi.framework.quartz.exception.TaskException;

/**
 * 定时任务反射实现接口类
 */
public interface ITaskInvok {

    void invokeMethod(IJob job) throws TaskException;
}
