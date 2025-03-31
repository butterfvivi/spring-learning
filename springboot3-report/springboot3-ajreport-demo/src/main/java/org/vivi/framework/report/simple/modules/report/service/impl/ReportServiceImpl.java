package org.vivi.framework.report.simple.modules.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.report.simple.common.enums.ReportTypeEnum;
import org.vivi.framework.report.simple.common.exception.BusinessExceptionBuilder;
import org.vivi.framework.report.simple.modules.dashboard.entity.ReportDashboard;
import org.vivi.framework.report.simple.modules.dashboardwidget.entity.ReportDashboardWidget;
import org.vivi.framework.report.simple.modules.report.entity.Report;
import org.vivi.framework.report.simple.modules.reportexcel.entity.ReportExcel;
import org.vivi.framework.report.simple.modules.dashboard.dao.ReportDashboardMapper;
import org.vivi.framework.report.simple.modules.dashboardwidget.dao.ReportDashboardWidgetMapper;
import org.vivi.framework.report.simple.modules.reportexcel.dao.ReportExcelMapper;
import org.vivi.framework.report.simple.modules.report.dao.ReportMapper;

import org.vivi.framework.report.simple.modules.report.service.ReportService;
import org.vivi.framework.report.simple.web.dto.ReportDto;

import java.util.List;


@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private ReportDashboardMapper reportDashboardMapper;
    @Autowired
    private ReportDashboardWidgetMapper reportDashboardWidgetMapper;
    @Autowired
    private ReportExcelMapper reportExcelMapper;

    /**
     * 下载次数+1
     *
     * @param reportCode
     */
    @Override
    public void downloadStatistics(String reportCode) {
        Report report = this.baseMapper.selectOne(new LambdaQueryWrapper<Report>().eq(Report::getReportCode, reportCode));
        if (null != report) {
            Long downloadCount = report.getDownloadCount();
            if (null == downloadCount) {
                downloadCount = 1L;
            }else {
                downloadCount++;
            }
            report.setDownloadCount(downloadCount);
            updateById(report);
        }

    }

    @Override
    public void copy(ReportDto dto) {
        if (null == dto.getId()) {
            throw BusinessExceptionBuilder.build("500", "id");
        }
        if (StringUtils.isBlank(dto.getReportCode())) {
            throw BusinessExceptionBuilder.build("500", "报表编码");
        }
        Report report = this.baseMapper.selectById(dto.getId());
        String reportCode = report.getReportCode();
        Report copyReport = copyReport(report, dto);
        //复制主表数据
        this.baseMapper.insert(copyReport);
        String copyReportCode = copyReport.getReportCode();
        String reportType = report.getReportType();
        switch (ReportTypeEnum.valueOf(reportType)) {
            case report_screen:
                //查询看板
                ReportDashboard reportDashboard =
                        reportDashboardMapper.selectOne(new LambdaQueryWrapper<ReportDashboard>().eq(ReportDashboard::getReportCode, reportCode));
                if (null != reportDashboard) {
                    reportDashboard.setId(null);
                    reportDashboard.setReportCode(copyReportCode);
                    reportDashboardMapper.insert(reportDashboard);
                }

                //查询组件
                List<ReportDashboardWidget> reportDashboardWidgetList =
                        reportDashboardWidgetMapper.selectList(new LambdaQueryWrapper<ReportDashboardWidget>().eq(ReportDashboardWidget::getReportCode, reportCode));
                if (!CollectionUtils.isEmpty(reportDashboardWidgetList)) {
                    String finalCopyReportCode = copyReportCode;
                    reportDashboardWidgetList.forEach(reportDashboardWidget -> {
                        reportDashboardWidget.setId(null);
                        reportDashboardWidget.setReportCode(finalCopyReportCode);
                        reportDashboardWidgetMapper.insert(reportDashboardWidget);
                    });
                }

                break;
            case report_excel:
                ReportExcel reportExcel =
                        reportExcelMapper.selectOne(new LambdaQueryWrapper<ReportExcel>().eq(ReportExcel::getReportCode, reportCode));
                if (null != reportExcel) {
                    reportExcel.setId(null);
                    reportExcel.setReportCode(copyReportCode);
                    reportExcelMapper.insert(reportExcel);
                }

                break;
            default:
        }
    }

    private Report copyReport(Report report, ReportDto dto){
        //复制主表数据
        Report copyReport = new Report();
        BeanUtils.copyProperties(report, copyReport);
        copyReport.setReportCode(dto.getReportCode());
        copyReport.setReportName(dto.getReportName());
        copyReport.setId(null);
        return copyReport;
    }


}
