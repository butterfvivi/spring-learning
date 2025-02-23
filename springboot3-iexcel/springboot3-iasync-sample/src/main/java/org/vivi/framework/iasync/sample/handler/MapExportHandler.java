package org.vivi.framework.iasync.sample.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.vivi.framework.iasync.sample.service.ExcelUserService;
import org.vivi.framework.iasyncexcel.core.annotation.ExcelHandle;
import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.exporter.ExportPage;
import org.vivi.framework.iasyncexcel.core.handler.ExportHandler;
import org.vivi.framework.iasyncexcel.core.model.DataParam;
import org.vivi.framework.iasyncexcel.core.model.ExcelContext;

import java.util.List;
import java.util.Map;

@ExcelHandle
public class MapExportHandler implements ExportHandler<List<Map<String, Object>>> {

    @Autowired
    private ExcelUserService excelUserService;

    @Override
    public void init(ExcelContext ctx, DataParam param) {
        ExportContext context = (ExportContext) ctx;
        WriteSheet sheet = EasyExcel.writerSheet(0, "第一个sheet").head(List.class).build();
        context.setWriteSheet(sheet);
    }

    @Override
    public ExportPage exportData(int startPage, int limit, DataExportParam param) {
        //IPage<Map<String, Object>> page = new Page();
        //IPage<Map<String, Object>> iPageList = excelUserService.selectList(page);
        List<Map<String, Object>> list = excelUserService.selectList();

        //List<User> transform = ExportListUtil.transform(list.size(), List.class);
        ExportPage exportPage = new ExportPage<>();
        exportPage.setRecords(list);

        return exportPage;
    }
}
