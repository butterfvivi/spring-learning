package org.vivi.framework.iasyncexcel.core.handler;


import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.exporter.ExportPage;

import java.util.List;

public interface ExportHandler <T> extends Handler {

    /**
     * 分页导出
     * @param startPage
     * @param limit
     * @param param
     * @return
     */
    ExportPage<T> exportData(int startPage,int limit,DataExportParam param);

    /**每页开始前在exportData执行前执行
     * @param ctx 导出上下文，你可以在开始前进行修改
     * @param param 导出参数
     */
    default void beforePerPage(ExportContext ctx, DataExportParam param) {
    }

    /**exportData执行后
     * @param list 得到的数据
     * @param ctx   上下文
     * @param param 外部入参
     */
    default void afterPerPage(List<T> list, ExportContext ctx, DataExportParam param) {
    }
}
