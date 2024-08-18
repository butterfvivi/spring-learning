package org.vivi.framework.iasyncexcel.core.support;

import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;

public interface ExportTaskSupport {

    /**
     * 创建到处任务
     * @param param
     * @return
     */
    ExcelTask createTask(DataExportParam param);

    /**
     * 导出处理
     * @param context
     */
    void onExport(ExportContext context);

    /**
     * 完成导出
     * @param context
     */
    void onComplete(ExportContext context);

    /**
     * 导出失败处理
     * @param context
     */
    void onError(ExportContext context);
}
