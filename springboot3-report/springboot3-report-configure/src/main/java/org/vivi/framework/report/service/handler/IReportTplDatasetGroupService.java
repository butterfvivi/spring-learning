package org.vivi.framework.report.service.handler;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.model.reporttpldatasetgroup.ReportTplDatasetGroup;

import java.util.List;

public interface IReportTplDatasetGroupService extends IService<ReportTplDatasetGroup> {

    /**
     * @Title: tablePagingQuery
     * @Description: 表格分页查询
     */
    List<ReportTplDatasetGroup> tablePagingQuery(ReportTplDatasetGroup model);

    /**
     *<p>Title: getDetail</p>
     *<p>Description: 获取详情</p>
     */
    BaseEntity getDetail(Long id);

    /**
     *<p>Title: insert</p>
     *<p>Description: 新增数据</p>
     */
    BaseEntity insert(ReportTplDatasetGroup model);

    /**
     *<p>Title: update</p>
     *<p>Description: 更新数据</p>
     */
    BaseEntity update(ReportTplDatasetGroup model);

    /**
     *<p>Title: delete</p>
     *<p>Description: 单条删除数据</p>
     */
    BaseEntity delete(Long id);

    /**
     *<p>Title: deleteBatch</p>
     *<p>Description: 批量删除数据</p>
     */
    BaseEntity deleteBatch(List<Long> ids);
}
