package org.vivi.framework.iasyncexcel.core.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vivi.framework.iasyncexcel.common.enums.ExcelStatusEnums;
import org.vivi.framework.iasyncexcel.common.enums.ExcelTypeEnums;
import org.vivi.framework.iasyncexcel.core.importer.ImportContext;
import org.vivi.framework.iasyncexcel.core.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;
import org.vivi.framework.iasyncexcel.core.service.IStorageService;
import org.vivi.framework.iasyncexcel.core.service.TaskService;

import java.time.LocalDateTime;

@Service
public class AsyncImportTaskSupport implements ImportTaskSupport {

    private final static Logger log = LoggerFactory.getLogger(AsyncImportTaskSupport.class);

    IStorageService storageService;
    TaskService taskService;
    public static String IMPORT_ERROR_PREFIX = "import-error-";
    public static String XLSX_SUFFIX = ".xlsx";

    public AsyncImportTaskSupport(IStorageService storageService, TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ExcelTask createTask(ImportDataParam param) {
        ExcelTask excelTask = new ExcelTask();

        excelTask.setStatus(ExcelStatusEnums.SUBMETTED.getCode());
        excelTask.setType(ExcelTypeEnums.IMPORT.getCode());
        excelTask.setSourceFile(param.getSourceFile());
        excelTask.setFileName(param.getFilename());
        excelTask.setStartTime(LocalDateTime.now());
        taskService.save(excelTask);
        return excelTask;
    }

    @Override
    public void beforeImport(ImportDataParam param) {

    }

    @Override
    public void onImport(ImportContext context) {
        ExcelTask excelTask = context.getTask();
        excelTask.setStatus(ExcelStatusEnums.IN_PROCESS.getCode());
        excelTask.setFailedCount(context.getFailCount());
        excelTask.setSuccessCount(context.getSuccessCount());
        excelTask.setTotalCount(context.getTotalCount());
        taskService.updateById(excelTask);
    }

    @Override
    public void onError(ImportContext context) {
        close(context);
        ExcelTask excelTask = context.getTask();
        excelTask.setStatus(ExcelStatusEnums.FAILED.getCode());
        excelTask.setFailedCount(context.getFailCount());
        excelTask.setSuccessCount(context.getSuccessCount());
        excelTask.setEndTime(LocalDateTime.now());
        excelTask.setTotalCount(context.getTotalCount());
        excelTask.setFailedFileUrl(context.getErrorFile());
        excelTask.setFailedMessage(context.getFailMessage());
        taskService.updateById(excelTask);
        if (log.isDebugEnabled()) {
            log.debug("task import error");
        }
    }

    @Override
    public void onComplete(ImportContext context) {
        ExcelTask excelTask = context.getTask();
        excelTask.setStatus(ExcelStatusEnums.SUCCESS.getCode());
        excelTask.setFailedCount(context.getFailCount());
        excelTask.setSuccessCount(context.getSuccessCount());
        excelTask.setTotalCount(context.getTotalCount());
        taskService.updateById(excelTask);
        log.info("task import complete");
    }

    public void close(ImportContext ctx) {
        if (ctx.getExcelWriter() != null) {
            ctx.getExcelWriter().finish();
        }
        if (ctx.getOutputStream() != null) {
            try {
                ctx.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (ctx.getInputStream() != null) {
            try {
                if (ctx.getFuture() != null) {
                    ctx.setErrorFile(ctx.getFuture().get());
                }
                ctx.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
