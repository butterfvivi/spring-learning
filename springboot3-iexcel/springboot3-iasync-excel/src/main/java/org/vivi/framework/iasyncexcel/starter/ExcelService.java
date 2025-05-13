package org.vivi.framework.iasyncexcel.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.vivi.framework.iasyncexcel.common.enums.FileTypeEnums;
import org.vivi.framework.iasyncexcel.core.exporter.AsyncExportProcessor;
import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.handler.ExportHandler;
import org.vivi.framework.iasyncexcel.core.handler.ImportHandler;
import org.vivi.framework.iasyncexcel.core.importer.AsyncImportProcessor;
import org.vivi.framework.iasyncexcel.core.importer.ImportContext;
import org.vivi.framework.iasyncexcel.core.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;
import org.vivi.framework.iasyncexcel.core.support.ExportTaskSupport;
import org.vivi.framework.iasyncexcel.core.support.ImportTaskSupport;
import org.vivi.framework.iasyncexcel.starter.context.config.ExcelThreadPool;

import java.util.Objects;

/**
 * Service for handling Excel import and export operations
 */
@Slf4j
@Service
public class ExcelService {

    private static final String EXPORT_PREFIX = "导出";

    private final ExcelThreadPool threadPool;
    private final SpringExcelContext context;

    public ExcelService(ExcelThreadPool threadPool, SpringExcelContext context) {
        this.threadPool = Objects.requireNonNull(threadPool, "Thread pool cannot be null");
        this.context = Objects.requireNonNull(context, "Excel context cannot be null");
    }

    /**
     * Perform an asynchronous Excel import operation
     *
     * @param cls The ImportHandler class to use
     * @param param Import parameters
     * @return Task ID for tracking the import
     */
    public Long doImport(Class<? extends ImportHandler> cls, ImportDataParam param) {
        Objects.requireNonNull(cls, "Handler class cannot be null");
        Objects.requireNonNull(param, "Import parameters cannot be null");

        ImportHandler handler = context.getInstance(cls);
        ImportTaskSupport support = context.getInstance(ImportTaskSupport.class);

        if (support == null) {
            throw new IllegalStateException("Failed to obtain ImportTaskSupport bean");
        }

        ExcelTask task = support.createTask(param);
        log.info("添加导入任务: taskId={}", task.getId());

        ImportContext ctx = buildImportContext(task, param);
        AsyncImportProcessor processor = new AsyncImportProcessor(threadPool.getExecutor());
        processor.importProcess(handler, support, ctx, param);

        return task.getId();
    }

    /**
     * Perform an asynchronous Excel export operation with multiple handlers
     *
     * @param param Export parameters
     * @param clses Export handler classes (one per sheet)
     * @return Task ID for tracking the export
     */
    public Long doExport(DataExportParam param, Class<? extends ExportHandler>... clses) {
        Objects.requireNonNull(param, "Export parameters cannot be null");
        Objects.requireNonNull(clses, "Handler classes cannot be null");

        if (clses.length == 0) {
            throw new IllegalArgumentException("At least one export handler must be provided");
        }

        ExportHandler[] handlers = new ExportHandler[clses.length];
        for (int i = 0; i < clses.length; i++) {
            handlers[i] = context.getInstance(clses[i]);
        }

        ExportTaskSupport support = context.getInstance(ExportTaskSupport.class);
        if (support == null) {
            throw new IllegalStateException("Failed to obtain ExportTaskSupport bean");
        }

        ExcelTask task = support.createTask(param);
        ExportContext ctx = buildExportContext(task, param);

        AsyncExportProcessor processor = new AsyncExportProcessor(threadPool.getExecutor());
        processor.exportData(support, param, ctx, handlers);

        return task.getId();
    }

    /**
     * Build import context from task and parameters
     */
    private ImportContext buildImportContext(ExcelTask task, ImportDataParam param) {
        ImportContext ctx = new ImportContext();
        ctx.setTask(task);
        ctx.setFileName(param.getFilename());
        ctx.setErrorHeadClass(param.getModel());
        ctx.setValidMaxRows(param.isValidMaxRows());
        ctx.setMaxRows(param.getMaxRows());
        ctx.setValidHead(param.isValidHead());
        return ctx;
    }

    /**
     * Build export context from task and parameters
     */
    private ExportContext buildExportContext(ExcelTask task, DataExportParam param) {
        ExportContext ctx = new ExportContext();
        ctx.setTask(task);
        ctx.setLimit(param.getLimit());
        ctx.setHeadClass(param.getHeadClass());
        ctx.setDynamicHead(param.isDynamicHead());
        ctx.setHeadList(param.getHeadList());
        ctx.setWriteHandlers(param.getWriteHandlers());
        ctx.setConverters(param.getConverters());
        ctx.setSheetName(param.getSheetName());
        ctx.setFileName(buildExportFileName(task.getId(), param.getExportFileName()));
        return ctx;
    }

    /**
     * Build export file name with appropriate extension
     */
    private String buildExportFileName(Long taskId, String exportFileName) {
        StringBuilder sb = new StringBuilder(EXPORT_PREFIX).append(taskId).append("-");

        if (StringUtils.isEmpty(exportFileName)) {
            return sb.append(FileTypeEnums.XLSX.getCode()).toString();
        }

        if (exportFileName.contains(".")) {
            String extension = exportFileName.substring(exportFileName.lastIndexOf("."));
            if (FileTypeEnums.XLSX.getCode().equals(extension)) {
                return sb.append(exportFileName).toString();
            } else {
                return sb.append(exportFileName).toString();
            }
        } else {
            return sb.append(exportFileName).append(FileTypeEnums.XLSX.getCode()).toString();
        }
    }
}
