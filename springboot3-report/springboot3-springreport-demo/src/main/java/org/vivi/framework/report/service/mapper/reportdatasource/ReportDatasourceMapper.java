package org.vivi.framework.report.service.mapper.reportdatasource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.service.model.reportdatasource.ReportDatasource;
import java.util.List;

@Mapper
public interface ReportDatasourceMapper extends BaseMapper<ReportDatasource>{

   /**
    * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
    * @param model 包含查询条件的对象实体
    * @return 实体集合
    */
   List<ReportDatasource> searchDataLike(final ReportDatasource model);
}
