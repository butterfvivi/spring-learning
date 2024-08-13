package org.vivi.framework.iasyncexcel.excel.importer;

import org.vivi.framework.iasyncexcel.model.ExcelTask;

public interface ImportTaskSupport {

    /**
     * 创建任务
     * @param param
     * @return
     */
    ExcelTask createTask(ImportDataParam param);

    /**
     * d导入前处理
     * @param param
     */
    void beforeImport(ImportDataParam param);

    /**
     * 执行导入
     * @param context
     */
    void onImport(ImportContext context);

    /**
     * 导入处理期间出现error
     * @param context
     */
    void onError(ImportContext context);

    /**
     * 导入完成
     * @param context
     */
    void onComplete(ImportContext context);
}
