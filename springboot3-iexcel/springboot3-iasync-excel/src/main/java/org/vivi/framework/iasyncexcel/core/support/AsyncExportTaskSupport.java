package org.vivi.framework.iasyncexcel.core.support;

import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.iasyncexcel.common.enums.ExcelStatusEnums;
import org.vivi.framework.iasyncexcel.common.enums.ExcelTypeEnums;
import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;
import org.vivi.framework.iasyncexcel.core.service.TaskService;

import java.time.LocalDateTime;

@Slf4j
public class AsyncExportTaskSupport implements ExportTaskSupport{

    private TaskService taskService;

    public AsyncExportTaskSupport(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ExcelTask createTask(DataExportParam param) {
        ExcelTask excelTask = new ExcelTask();

        excelTask.setStatus(ExcelStatusEnums.SUBMETTED.getCode());
        excelTask.setType(ExcelTypeEnums.EXPORT.getCode());
        excelTask.setFileName(param.getExportFileName());
        excelTask.setStartTime(LocalDateTime.now());
        excelTask.setCreateUserCode(param.getCreateUserCode());
        excelTask.setBusinessCode(param.getBusinessCode());
        taskService.save(excelTask);
        return excelTask;
    }

    @Override
    public void onExport(ExportContext context) {
        ExcelTask excelTask = context.getTask();
        excelTask.setStatus(ExcelStatusEnums.IN_PROCESS.getCode());
        excelTask.setFailedCount(context.getFailCount());
        excelTask.setSuccessCount(context.getSuccessCount());
        excelTask.setTotalCount(context.getTotalCount());
        taskService.updateById(excelTask);
    }

    @Override
    public void onComplete(ExportContext context) {
        ExcelTask excelTask = context.getTask();
        excelTask.setStatus(ExcelStatusEnums.SUCCESS.getCode());
        excelTask.setFailedCount(context.getFailCount());
        excelTask.setSuccessCount(context.getSuccessCount());
        excelTask.setTotalCount(context.getTotalCount());
        taskService.updateById(excelTask);
    }

    @Override
    public void onError(ExportContext context) {
        close(context);
        ExcelTask excelTask = context.getTask();
        excelTask.setStatus(ExcelStatusEnums.FAILED.getCode());
        excelTask.setFailedCount(context.getFailCount());
        excelTask.setSuccessCount(context.getSuccessCount());
        excelTask.setEndTime(LocalDateTime.now());
        excelTask.setTotalCount(context.getTotalCount());
        excelTask.setFailedMessage(context.getFailMessage());
        taskService.updateById(excelTask);
        if (log.isDebugEnabled()) {
            log.debug("task import error");
        }
    }

    public void close(ExportContext ctx) {
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
                ctx.setResultFile(ctx.getFuture().get());
                ctx.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
