package org.vivi.framework.iasync.thread.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.iasync.thread.entity.ExportTask;

import java.util.Date;

/**
 * 导出任务服务类
 */
public interface ExportTaskService extends IService<ExportTask> {
    
    long insertTaskInfo(String module, String fileName, String filePath, String identity, String userName, Date startTime);
    
    long updateTaskInfo(int status, long identity, String reason);

    long deleteByTaskId(String id);
}
