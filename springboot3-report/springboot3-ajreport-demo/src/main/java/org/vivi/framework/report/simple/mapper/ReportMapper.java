package org.vivi.framework.report.simple.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.simple.entity.report.Report;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {
}
