package org.vivi.framework.iasyncexcel.core.support;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.vivi.framework.iasyncexcel.common.enums.ExcelStatusEnums;
import org.vivi.framework.iasyncexcel.common.enums.ExcelTypeEnums;
import org.vivi.framework.iasyncexcel.common.utils.ExceptionUtil;
import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.model.ExcelTask;
import org.vivi.framework.iasyncexcel.core.service.IStorageService;
import org.vivi.framework.iasyncexcel.core.service.TaskService;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.FutureTask;

@Slf4j
public class AsyncExportTaskSupport implements ExportTaskSupport{

    private TaskService taskService;
    IStorageService storageService;
    private final static String tempPath="/Users/vivi/IdeaProjects/spring-learning/springboot3-iexcel/springboot3-iasync-sample/src/main/resources/excel/";

    public AsyncExportTaskSupport(IStorageService storageService, TaskService taskService) {
        this.storageService = storageService;
        this.taskService = taskService;
    }

    @Override
    public ExcelTask createTask(DataExportParam param) {
        ExcelTask excelTask = new ExcelTask();

        excelTask.setStatus(ExcelStatusEnums.SUBMETTED.getCode());
        excelTask.setType(ExcelTypeEnums.EXPORT.getCode());
        excelTask.setFileName(param.getExportFileName());
        excelTask.setStartTime(LocalDateTime.now());
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
    public void onWrite(Collection<?> dataList, ExportContext ctx) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }

        // 构建导出目录
        File exportDir = new File(tempPath);
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            throw new RuntimeException("无法创建导出目录: " + tempPath);
        }

        // 构建文件名
        String fileName = ctx.getFileName();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            fileName = ctx.getTask().getId() + ".xlsx";
        }
        File exportFile = new File(exportDir, fileName);
        ctx.setFileName(fileName);
        if (ctx.getOutputStream() == null) {
            PipedOutputStream pos = new PipedOutputStream();
            try {
                PipedInputStream pis = new PipedInputStream(pos);
                ctx.setInputStream(pis);
                //此处单独起线程避免线程互相等待死锁
                FutureTask<String> futureTask = new FutureTask<>(
                        () -> storageService.write(ctx.getFileName(), ctx.getInputStream()));
                Thread thread = new Thread(futureTask);
                thread.setDaemon(true);
                thread.start();

                new Thread(futureTask).start();
                ctx.setFuture(futureTask);
            } catch (IOException e) {
                ExceptionUtil.wrap2Runtime(e);
            }
            ctx.setOutputStream(pos);
        }


        //创建excel
        if (ctx.getExcelWriter() == null) {
            ExcelWriterBuilder writerBuilder = EasyExcel.write(exportFile)
                    .excelType(ExcelTypeEnum.XLSX).autoCloseStream(false);
            ExcelWriter excelWriter = writerBuilder.build();

            ctx.setExcelWriter(excelWriter);
        }
        //创建sheet 此代码块将在后续某个版本的迭代中移除，sheet将由handler的init方法创建
        if (ctx.getWriteSheet()==null){
            ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet(0)
                    .sheetName(ctx.getSheetName());
            //动态表头以sheet为单位
            if (ctx.isDynamicHead()) {
                sheetBuilder.head(ctx.getHeadList());
            } else {
                sheetBuilder.head(ctx.getHeadClass());
            }
            //自定义样式以sheet为单位
            if (ctx.getWriteHandlers() != null && ctx.getWriteHandlers().size() > 0) {
                for (WriteHandler writeHandler : ctx.getWriteHandlers()) {
                    sheetBuilder.registerWriteHandler(writeHandler);
                }
            }
            //自定义类型转换器以sheet为单位
            if (ctx.getConverters() != null && ctx.getConverters().size() > 0) {
                for (Converter<?> converter : ctx.getConverters()) {
                    sheetBuilder.registerConverter(converter);
                }
            }
            WriteSheet writeSheet = sheetBuilder.build();
            ctx.setWriteSheet(writeSheet);
        }

        ctx.getExcelWriter().write(dataList, ctx.getWriteSheet());
        ctx.getExcelWriter().finish();

        // 设置结果路径供后续使用
        ctx.setResultFile(exportFile.getAbsolutePath());
    }

    @Override
    public void onComplete(ExportContext context) {
        ExcelTask excelTask = context.getTask();
        excelTask.setStatus(ExcelStatusEnums.SUCCESS.getCode());
        excelTask.setFailedCount(context.getFailCount());
        excelTask.setSuccessCount(context.getSuccessCount());
        excelTask.setTotalCount(context.getTotalCount());
        excelTask.setEndTime(LocalDateTime.now());
        excelTask.setFileUrl(context.getResultFile());
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
