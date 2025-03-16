package org.vivi.framework.report.service.mapper.luckysheetreportcell;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.service.model.luckysheetreportcell.LuckysheetReportCell;

import java.util.List;

@Mapper
public interface LuckysheetReportCellMapper extends BaseMapper<LuckysheetReportCell>{

   /**
    * @MethodName: getVariableCells
    * @Description: 获取sheet下的动态单元格数据
    */
   List<LuckysheetReportCell> getVariableCells(final LuckysheetReportCell model);
}
