
package org.vivi.framework.report.simple.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.report.simple.entity.dashboardwidget.ReportDashboardWidget;

/**
* @desc ReportDashboardWidget 大屏看板数据渲染服务接口
**/
public interface ReportDashboardWidgetService extends IService<ReportDashboardWidget> {

    /***
     * 查询详情
     *
     * @param id
     */
    ReportDashboardWidget getDetail(Long id);
}
