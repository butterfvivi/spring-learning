package org.vivi.framework.report.service.mapper.reporttpldataset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.report.service.model.reporttpldataset.ReportTplDataset;

import java.util.List;

@Mapper
public interface ReportTplDatasetMapper extends BaseMapper<ReportTplDataset>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     */
    List<ReportTplDataset> searchDataLike(final ReportTplDataset model);
}
