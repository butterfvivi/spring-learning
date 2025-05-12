package org.vivi.framework.iasyncexcel.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
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

@Slf4j
@Component
public class ExcelService {

    private ExcelThreadPool threadPool;

    private SpringExcelContext context;


    public ExcelService(ExcelThreadPool threadPool, SpringExcelContext context) {
        this.threadPool = threadPool;
        this.context = context;
    }

    public Long doImport(Class<? extends ImportHandler> cls, ImportDataParam param){
        ImportHandler handler = context.getInstance(cls);
        ImportTaskSupport support = context.getInstance(ImportTaskSupport.class);
        ExcelTask task = support.createTask(param);
        log.info("添加任务taskId:{}",task.getId());
        ImportContext ctx=new ImportContext();
        ctx.setTask(task);
        ctx.setFileName(param.getFilename());
        ctx.setErrorHeadClass(param.getModel());
        ctx.setValidMaxRows(param.isValidMaxRows());
        ctx.setMaxRows(param.getMaxRows());
        ctx.setValidHead(param.isValidHead());
        AsyncImportProcessor asyncExcelImporter = new AsyncImportProcessor(threadPool.getExecutor());
        asyncExcelImporter.importProcess(handler,support,ctx,param);
        return task.getId();
    }

    public Long doExport(Class<? extends ExportHandler> cls, DataExportParam param){
        String filePrefix = "导出";
        ExportHandler handler = context.getInstance(cls);
        ExportTaskSupport support = context.getInstance(ExportTaskSupport.class);
        ExcelTask task = support.createTask(param);
        ExportContext ctx = new ExportContext();
        ctx.setTask(task);
        ctx.setLimit(param.getLimit());
        ctx.setHeadClass(param.getHeadClass());
        ctx.setHeadList(param.getHeadList());
        ctx.setWriteHandlers(param.getWriteHandlers());
        ctx.setConverters(param.getConverters());
        ctx.setSheetName(param.getSheetName());
        String exportFileName = param.getExportFileName();
        StringBuilder sb = new StringBuilder(filePrefix).append(task.getId()).append("-");
        if (StringUtils.isEmpty(exportFileName)){
            sb.append(FileTypeEnums.XLSX.getCode());
        }else {
            if (exportFileName.lastIndexOf(".") != -1){
                String extension = exportFileName.substring(exportFileName.lastIndexOf("."));
                if (FileTypeEnums.XLSX.getCode().equals(extension)){
                    sb.append(exportFileName).append(FileTypeEnums.XLSX.getCode());
                }else {
                    sb.append(exportFileName);
                }
            }else {
                sb.append(exportFileName).append(FileTypeEnums.XLSX.getCode());
            }
        }
        ctx.setFileName(sb.toString());
        AsyncExportProcessor asyncExportProcessor = new AsyncExportProcessor(threadPool.getExecutor());
        asyncExportProcessor.exportData(handler,support,param,ctx);
        return task.getId();
    }

    public Long doExport(DataExportParam param, Class<? extends ExportHandler>... clses){
        String filePrefix = "导出";
        ExportHandler[] handlers = new ExportHandler[clses.length];
        for (int i = 0; i < clses.length; i++) {
            ExportHandler handler = context.getInstance(clses[i]);
            handlers[i] = handler;
        }
        ExportTaskSupport support = context.getInstance(ExportTaskSupport.class);
        ExcelTask task = support.createTask(param);
        ExportContext ctx = new ExportContext();
        ctx.setTask(task);
        ctx.setLimit(param.getLimit());
        ctx.setHeadClass(param.getHeadClass());
        ctx.setDynamicHead(param.isDynamicHead());
        ctx.setHeadList(param.getHeadList());
        ctx.setWriteHandlers(param.getWriteHandlers());
        ctx.setConverters(param.getConverters());
        ctx.setSheetName(param.getSheetName());
        String exportFileName = param.getExportFileName();
        StringBuilder sb = new StringBuilder(filePrefix).append(task.getId()).append("-");
        if (StringUtils.isEmpty(exportFileName)){
            sb.append(FileTypeEnums.XLSX.getCode());
        }else {
            if (exportFileName.lastIndexOf(".") != -1){
                String extension = exportFileName.substring(exportFileName.lastIndexOf("."));
                if (FileTypeEnums.XLSX.getCode().equals(extension)){
                    sb.append(exportFileName).append(FileTypeEnums.XLSX.getCode());
                }else {
                    sb.append(exportFileName);
                }
            }else {
                sb.append(exportFileName).append(FileTypeEnums.XLSX.getCode());
            }
        }
        ctx.setFileName(sb.toString());
        AsyncExportProcessor asyncExportProcessor = new AsyncExportProcessor(threadPool.getExecutor());
        asyncExportProcessor.exportData(support,param,ctx,handlers);
        return task.getId();
    }
}
