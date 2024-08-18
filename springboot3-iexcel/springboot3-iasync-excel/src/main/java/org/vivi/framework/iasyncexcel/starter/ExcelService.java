package org.vivi.framework.iasyncexcel.starter;

import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.iasyncexcel.core.importer.*;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;
import org.vivi.framework.iasyncexcel.starter.context.config.ExcelThreadPool;

@Slf4j
//@Component
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
        AsyncExcelProcessor asyncExcelImporter = new AsyncExcelProcessor(threadPool.getExecutor());
        asyncExcelImporter.importProcess(handler,support,ctx,param);
        return task.getId();
    }
}
