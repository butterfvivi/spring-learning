package org.vivi.framework.iasyncexcel.core.exporter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.vivi.framework.iasyncexcel.common.utils.ExceptionUtil;
import org.vivi.framework.iasyncexcel.core.handler.ExportHandler;
import org.vivi.framework.iasyncexcel.core.support.ExportTaskSupport;

import java.rmi.server.ExportException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
/**
 * Processor for handling asynchronous Excel data exports with support for
 * single and multi-sheet exports.
 */
public class AsyncExportProcessor {

    private final ExecutorService executor;
    private static final String EXPORT_ERROR_MSG = "系统异常，联系管理员";

    public AsyncExportProcessor(ExecutorService executor) {
        this.executor = Objects.requireNonNull(executor, "Executor service cannot be null");
    }


    /**
     * Export data using multiple handlers (one handler per sheet)
     *
     * @param support Task support for export operations
     * @param param Export parameters
     * @param ctx Export context
     * @param handlers Export handlers for each sheet
     * @return CompletableFuture that completes when export finishes
     */
    public CompletableFuture<Void> exportData(ExportTaskSupport support, DataExportParam param,
                                            ExportContext ctx, ExportHandler... handlers) {
        validateParameters(support, param, ctx, handlers);

        return CompletableFuture.runAsync(() -> {
            try {
                if (handlers == null || handlers.length == 0) {
                    throw new RuntimeException("未设置导出处理类");
                }
                int sheetNo = 0;
                long totalEstimateCount = 0;

                for (ExportHandler handler : handlers) {
                    handler.init(ctx, param);
                    if (ctx.getWriteSheet() != null) {
                        ctx.getWriteSheet().setSheetNo(sheetNo++);
                    }

                    totalEstimateCount += processAllPages(handler, support, param, ctx);
                }

                ctx.getTask().setEstimateCount(totalEstimateCount);
                support.onComplete(ctx);
            } catch (Exception e) {
                handleExportError(e, ctx, support);
            } finally {
                for (ExportHandler handler : handlers) {
                    handler.callBack(ctx, param);
                }
            }
        }, executor);
    }

    /**
     * Process all pages for a single handler
     *
     * @return Total number of records processed
     */
    private long processAllPages(ExportHandler handler, ExportTaskSupport support,
                               DataExportParam param, ExportContext ctx) {
        int cursor = 1;
        ExportPage page = fetchPage(handler, support, param, ctx, cursor);

        if (page == null) {
            throw new RuntimeException("导出数据为空");
        }

        Long total = page.getTotal();
        long pageCount = calculatePageCount(total, page.getSize());

        for (cursor++; cursor <= pageCount; cursor++) {
            fetchPage(handler, support, param, ctx, cursor);
        }

        return total;
    }

    /**
     * Fetch a single page of export data
     */
    private ExportPage fetchPage(ExportHandler handler, ExportTaskSupport support,
                               DataExportParam param, ExportContext ctx, int pageNum) {
        support.onExport(ctx);
        try {
            handler.beforePerPage(ctx, param);
            int limit = ctx.getLimit() > 0 ? ctx.getLimit() : param.getLimit();
            ExportPage exportPage = handler.exportData(pageNum, limit, param);

            if (CollectionUtils.isNotEmpty(exportPage.getRecords())) {
                ctx.record(exportPage.getRecords().size());
                support.onWrite(exportPage.getRecords(), ctx);
                handler.afterPerPage(exportPage.getRecords(), ctx, param);
            }

            return exportPage;
        } catch (Exception e) {
            log.error("导出单页数据过程发生异常", e);
            throw ExceptionUtil.wrap2Runtime(e);
        }
    }

    /**
     * Calculate the total number of pages
     */
    private long calculatePageCount(Long total, Long pageSize) {
        return (total + pageSize - 1) / pageSize;
    }

    /**
     * Validate input parameters for multi-handler export
     */
    private void validateParameters(ExportTaskSupport support, DataExportParam param,
                                  ExportContext ctx, ExportHandler[] handlers) {
        Objects.requireNonNull(support, "ExportTaskSupport cannot be null");
        Objects.requireNonNull(param, "DataExportParam cannot be null");
        Objects.requireNonNull(ctx, "ExportContext cannot be null");
        Objects.requireNonNull(handlers, "Export handlers cannot be null");
    }

    /**
     * Handle export errors
     */
    private void handleExportError(Exception e, ExportContext ctx, ExportTaskSupport support) {
        log.error("导出异常", e);
        if (e instanceof ExportException) {
            ctx.setFailMessage(e.getMessage());
        } else {
            ctx.setFailMessage(EXPORT_ERROR_MSG);
        }
        support.onError(ctx);
    }
}
