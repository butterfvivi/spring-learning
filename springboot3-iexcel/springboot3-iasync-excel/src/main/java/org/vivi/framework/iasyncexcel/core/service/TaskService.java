package org.vivi.framework.iasyncexcel.core.service;

import org.vivi.framework.iasyncexcel.core.model.ExcelTask;

public interface TaskService {

    /**保存任务
     * @param task
     * @return
     */
    boolean save(ExcelTask task);

    /**根据id更新任务
     * @param task
     * @return
     */
    boolean updateById(ExcelTask task);
}
