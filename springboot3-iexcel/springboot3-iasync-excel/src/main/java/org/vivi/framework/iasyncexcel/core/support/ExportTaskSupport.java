package org.vivi.framework.iasyncexcel.core.support;

import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;

import java.util.Collection;

public interface ExportTaskSupport {

    /**创建任务
     * @param param
     * @return
     */
    ExcelTask createTask(DataExportParam param);

    /** 导出阶段
     * @param ctx
     */
    void onExport(ExportContext ctx);

    /** 写文件阶段
     * @param dataList
     * @param ctx
     */
    void onWrite(Collection<?> dataList, ExportContext ctx);

    /**完成阶段
     * @param ctx
     */
    void onComplete(ExportContext ctx);

    /**失败处理阶段
     * @param ctx
     */
    void onError(ExportContext ctx);
}
