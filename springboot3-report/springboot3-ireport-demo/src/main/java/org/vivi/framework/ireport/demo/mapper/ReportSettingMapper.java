package org.vivi.framework.ireport.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.ireport.demo.model.report.ReportSetting;

@Mapper
public interface ReportSettingMapper extends BaseMapper<ReportSetting> {
}
