
package org.vivi.framework.report.simple.modules.dashboardwidget.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.simple.modules.dashboardwidget.entity.ReportDashboardWidget;
import org.vivi.framework.report.simple.modules.dashboardwidget.dao.ReportDashboardWidgetMapper;
import org.vivi.framework.report.simple.modules.dashboardwidget.service.ReportDashboardWidgetService;

/**
* @desc ReportDashboardWidget 大屏看板数据渲染服务实现
**/
@Service
public class ReportDashboardWidgetServiceImpl extends ServiceImpl<ReportDashboardWidgetMapper, ReportDashboardWidget> implements ReportDashboardWidgetService {

    @Autowired
    private ReportDashboardWidgetMapper reportDashboardWidgetMapper;


    @Override
    public ReportDashboardWidget getDetail(Long id) {
        ReportDashboardWidget reportDashboardWidget = reportDashboardWidgetMapper.selectById(id);
        return reportDashboardWidget;
    }
}
