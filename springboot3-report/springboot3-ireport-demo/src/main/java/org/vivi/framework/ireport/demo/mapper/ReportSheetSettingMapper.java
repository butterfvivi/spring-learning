package org.vivi.framework.ireport.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSet;

@Mapper
public interface ReportSheetSettingMapper extends BaseMapper<ReportSheetSet> {
}
