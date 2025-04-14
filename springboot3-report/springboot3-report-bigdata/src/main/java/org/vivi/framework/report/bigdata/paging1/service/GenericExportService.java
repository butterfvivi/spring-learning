package org.vivi.framework.report.bigdata.paging1.service;


import org.vivi.framework.report.bigdata.paging1.domain.Page;

import java.util.List;

public interface GenericExportService<T> {

    /**
     * 查询导出数据（自己定义连表查询）
     * @return List<T>
     */
    List<T> selectExcelList(Page page, T vo);

    /**
     * 获取总条数
     */

    Long getCount();
}
