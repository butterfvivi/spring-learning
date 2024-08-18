package org.vivi.framework.iasync.sample.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.vivi.framework.iasync.sample.model.User;
import org.vivi.framework.iasync.sample.service.ExcelUserService;
import org.vivi.framework.iasync.sample.utils.ExportListUtil;
import org.vivi.framework.iasyncexcel.core.annotation.ExcelHandle;
import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.exporter.ExportContext;
import org.vivi.framework.iasyncexcel.core.exporter.ExportPage;
import org.vivi.framework.iasyncexcel.core.handler.ExportHandler;
import org.vivi.framework.iasyncexcel.core.model.DataParam;
import org.vivi.framework.iasyncexcel.core.model.ExcelContext;

import java.util.List;

@ExcelHandle
public class UserExportHandler implements ExportHandler<User> {

    @Autowired
    private ExcelUserService excelUserService;

    @Override
    public void init(ExcelContext ctx, DataParam param) {
        ExportContext context = (ExportContext) ctx;
        WriteSheet sheet = EasyExcel.writerSheet(0, "第一个sheet").head(User.class).build();
        context.setWriteSheet(sheet);
    }


    @Override
    public ExportPage<User> exportData(int startPage, int limit, DataExportParam param) {
        IPage<User> page = new Page();
        IPage<User> iPageList = excelUserService.page(page);
        List<User> transform = ExportListUtil.transform(iPageList.getRecords(), User.class);
        ExportPage<User> exportPage = new ExportPage<>();
        exportPage.setCurrent(iPageList.getCurrent());
        exportPage.setTotal(page.getTotal());
        exportPage.setSize(page.getSize());
        exportPage.setRecords(transform);
        return exportPage;
    }
}
