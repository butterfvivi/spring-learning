package org.vivi.framework.report.service.mapper.reporttpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.service.model.reporttpl.ReportTpl;
import org.vivi.framework.report.service.web.dto.reporttpl.ReportTplDto;

import java.util.List;

@Mapper
public interface ReportTplMapper extends BaseMapper<ReportTpl>{


   /**
    * 	查询表格数据
    * @param model
    * @return
    */
   List<ReportTplDto> getTableList(final ReportTpl model);

}
