package org.vivi.framework.iasync.sample.handler;

import com.alibaba.excel.EasyExcel;
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
public class UserExportHandler implements ExportHandler<User>{

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
        IPage<User> iPage = new Page<>(startPage, limit);
        IPage<User> page = excelUserService.page(iPage);
        List<User> list = ExportListUtil.transform(page.getRecords(), User.class);
        ExportPage<User> result = new ExportPage<>();
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setRecords(list);
        return result;
    }
}
