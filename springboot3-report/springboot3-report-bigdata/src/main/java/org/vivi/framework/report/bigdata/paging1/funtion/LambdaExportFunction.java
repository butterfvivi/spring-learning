package org.vivi.framework.report.bigdata.paging1.funtion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.vivi.framework.report.bigdata.paging1.domain.Page;
import org.vivi.framework.report.bigdata.paging1.service.LambdaExportService;


import java.util.List;

public class LambdaExportFunction<T> implements ExportFunction<Page, List<T>> {

    private final LambdaExportService<T> lambdaExportService;

    private final LambdaQueryWrapper<T> lq;


    public LambdaExportFunction(LambdaExportService<T> singleTableExportService, LambdaQueryWrapper<T> lq) {
        this.lambdaExportService = singleTableExportService;
        this.lq = lq;
    }

    @Override
    public List<T> apply(Page page) {

        String sql = "LIMIT " + page.getStartIndex() + ", " + page.getPageSize();

        List<T> list = lambdaExportService.selectExcelList(sql, lq);
        return list;
    }

    @Override
    public Long getCount() {
        long count = lambdaExportService.getCount(lq);

        return count;

    }
}
