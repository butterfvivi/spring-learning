package org.vivi.framework.report.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.vivi.framework.report.bigdata.entity.Demo;
import org.vivi.framework.report.bigdata.mapper.DemoMapper;
import org.vivi.framework.report.bigdata.service.DemoService1;
import org.vivi.framework.report.bigdata.paging1.domain.Page;

import java.util.List;

public class DemoServiceImpl1 extends ServiceImpl<DemoMapper, Demo> implements DemoService1 {


    /**
     * 查询导出数据
     */

    @Override
    public List<Demo> selectExcelList(String sql, LambdaQueryWrapper<Demo> lq) {
        lq.last(sql);
        List<Demo> list = list(lq);
        return list;
    }

    @Override
    public Long getCount(LambdaQueryWrapper<Demo> lq) {
        long count = count(lq);
        return count;
    }

    @Override
    public List<Demo> selectExcelList(Page page, Demo vo) {
        return null;
    }

    @Override
    public Long getCount() {
        return null;
    }
}
