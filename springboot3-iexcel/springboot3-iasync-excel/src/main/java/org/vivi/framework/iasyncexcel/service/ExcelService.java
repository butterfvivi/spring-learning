package org.vivi.framework.iasyncexcel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.vivi.framework.iasyncexcel.common.context.SpringExcelContext;
import org.vivi.framework.iasyncexcel.excel.importer.*;
import org.vivi.framework.iasyncexcel.model.ExcelTask;

import java.util.concurrent.ExecutorService;

@Slf4j
//@Component
public class ExcelService {

    private ExecutorService executor;

    private SpringExcelContext context;

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
        AsyncExcelProcessor asyncExcelImporter = new AsyncExcelProcessor(executor);
        asyncExcelImporter.importProcess(handler,support,ctx,param);
        return task.getId();
    }
}
