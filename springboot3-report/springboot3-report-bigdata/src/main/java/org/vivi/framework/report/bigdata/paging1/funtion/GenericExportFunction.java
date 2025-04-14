package org.vivi.framework.report.bigdata.paging1.funtion;



import org.vivi.framework.report.bigdata.paging1.domain.Page;
import org.vivi.framework.report.bigdata.paging1.service.GenericExportService;

import java.util.List;


public class GenericExportFunction<T> implements ExportFunction<Page, List<T>> {

    private final GenericExportService<T> multiTableExportService;

    private final T vo;

    public GenericExportFunction(GenericExportService<T> multiTableExportService, T vo) {
        this.multiTableExportService = multiTableExportService;
        this.vo = vo;
    }

    @Override
    public List<T> apply(Page page) {
        List<T> list = multiTableExportService.selectExcelList(page, vo);
        return list;
    }

    @Override
    public Long getCount() {
        return null;
    }
}
