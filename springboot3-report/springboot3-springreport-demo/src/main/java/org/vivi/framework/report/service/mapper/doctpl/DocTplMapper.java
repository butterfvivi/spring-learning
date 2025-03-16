package org.vivi.framework.report.service.mapper.doctpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.service.model.doctpl.DocTpl;
import org.vivi.framework.report.service.web.dto.doctpl.DocTplDto;

import java.util.List;

@Mapper
public interface DocTplMapper extends BaseMapper<DocTpl>{


   /**
    * @MethodName: getTableList
    * @Description: 获取表格数据
    */
   List<DocTplDto> getTableList(final DocTpl model);
}
