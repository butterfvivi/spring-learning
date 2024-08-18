package org.vivi.framework.iasyncexcel.core.exporter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.function.TriFunction;
import org.vivi.framework.iasyncexcel.common.utils.ExceptionUtil;
import org.vivi.framework.iasyncexcel.core.handler.ExportHandler;
import org.vivi.framework.iasyncexcel.core.support.ExportTaskSupport;

import java.rmi.server.ExportException;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;

@Slf4j
public class AsyncExportProcessor {

    ExecutorService excutor;

    public AsyncExportProcessor(ExecutorService excutor) {
        this.excutor = excutor;
    }

    public void exportData(ExportHandler handler, ExportTaskSupport support,
                           DataExportParam param, ExportContext context){
        BiFunction<Integer,Integer,ExportPage> dataFunction = (start, limit) -> {
            support.onExport(context);
            handler.beforePerPage(context,param);
            ExportPage exportPage = handler.exportData(start, limit, param);
            if (exportPage == null){
                throw new RuntimeException("导出数据为空");
            }

            if (CollectionUtils.isEmpty(exportPage.getRecords())){
                return exportPage;
            }

            context.record(exportPage.getRecords().size());
            handler.afterPerPage(exportPage.getRecords(),context,param);
            return exportPage;
        };

        excutor.execute(() -> {
            try {
                handler.init(context, param);
                int cursor = 1;
                ExportPage page = dataFunction.apply(cursor, param.getLimit());
                Long total = page.getTotal();
                context.getTask().setEstimateCount(total);
                long pageNum = (total + page.getSize() - 1) / page.getSize();
                for (cursor++; cursor <= pageNum; cursor++) {
                    dataFunction.apply(cursor, param.getLimit());
                }
                support.onComplete(context);
            }catch (Exception e){
                log.error("导出异常", e);
                if (e instanceof ExportException) {
                    context.setFailMessage(e.getMessage());
                } else {
                    context.setFailMessage("系统异常，联系管理员");
                }
                support.onError(context);
            }
        });
    }

    public void exportData(ExportTaskSupport support,DataExportParam param,
                           ExportContext context,ExportHandler... handlers){
        TriFunction<ExportHandler,Integer,Integer,ExportPage> dataFunction = (h,start,limit) -> {
            support.onExport(context);
            try {
                h.beforePerPage(context, param);
                ExportPage exportPage = h.exportData(start, limit, param);

                if (CollectionUtils.isEmpty(exportPage.getRecords())) {
                    return exportPage;
                }
                context.record(exportPage.getRecords().size());
                h.afterPerPage(exportPage.getRecords(), context, param);
                return exportPage;
            }catch (Exception e){
                log.error("导出过程发生异常");
                throw ExceptionUtil.wrap2Runtime(e);
            }
        };

        excutor.execute(() -> {
            try {
                if (handlers == null || handlers.length == 0) {
                    throw new RuntimeException("未设置导出处理类");
                }
                int sheetNo = 0;
                for (ExportHandler handler : handlers) {
                    handler.init(context, param);
                    if (context.getWriteSheet() != null) {
                        context.getWriteSheet().setSheetNo(sheetNo);
                    }
                    sheetNo++;
                    int cursor = 1;
                    ExportPage page = dataFunction.apply(handler, cursor, param.getLimit());
                    Long total = page.getTotal();
                    context.getTask().setEstimateCount(total + context.getTask().getEstimateCount());
                    long pageNum = (total + page.getSize() - 1) / page.getSize();
                    for (cursor++; cursor <= pageNum; cursor++) {
                        dataFunction.apply(handler, cursor, context.getLimit());
                    }
                }
                support.onComplete(context);
            }catch (Exception e){
                log.error("导出异常", e);
                if (e instanceof ExportException) {
                    context.setFailMessage(e.getMessage());
                } else {
                    context.setFailMessage("系统异常，联系管理员");
                }
                support.onError(context);
            }
        });
    }
}
