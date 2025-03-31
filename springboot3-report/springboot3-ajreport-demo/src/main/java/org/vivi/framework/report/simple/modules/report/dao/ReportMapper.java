package org.vivi.framework.report.simple.modules.report.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.simple.modules.report.entity.Report;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {
}
