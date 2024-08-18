package org.vivi.framework.iasyncexcel.core.importer;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.vivi.framework.iasyncexcel.core.handler.ImportHandler;
import org.vivi.framework.iasyncexcel.core.listener.AsyncReadListener;
import org.vivi.framework.iasyncexcel.core.support.ImportTaskSupport;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class AsyncImportProcessor {

    ExecutorService executor;

    public AsyncImportProcessor(ExecutorService executor) {
        this.executor = executor;
    }

    public <T> void importProcess(ImportHandler<T> handler, ImportTaskSupport support, ImportContext context, ImportDataParam importDataParam){
        Consumer<List<T>> consumer = (datalist -> {
            support.onImport(context);
            List<ImportDto> importDtos = handler.importData(datalist, importDataParam);
            context.record(datalist.size(), importDtos.size());
        });

        AsyncReadListener asyncReadListener = new AsyncReadListener(consumer,support,context, importDataParam.getBatchSize());

        ExcelReader reader = EasyExcel.read(
                importDataParam.getStream(),
                importDataParam.getModel(),
                asyncReadListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        context.setReadSheet(readSheet);
        executor.execute(() -> {
            support.beforeImport(importDataParam);
            reader.read(context.getReadSheet());
            support.onImport(context);
        });
    }
}
