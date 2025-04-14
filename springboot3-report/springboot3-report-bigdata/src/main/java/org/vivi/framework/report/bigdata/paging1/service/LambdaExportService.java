package org.vivi.framework.report.bigdata.paging1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

public interface LambdaExportService<T> {

    /**
     * 查询导出数据 (单表查询)
     *
     * @param sql
     * @param lq
     * @return List<T>
     */
    List<T> selectExcelList(String sql, LambdaQueryWrapper<T> lq);

    /**
     * 获取总条数
     */

    Long getCount(LambdaQueryWrapper<T> lq);
}
