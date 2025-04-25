package org.vivi.framework.ireport.demo.service.report.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.mapper.ReportMapper;
import org.vivi.framework.ireport.demo.model.PageEntity;
import org.vivi.framework.ireport.demo.model.report.Report;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.report.ReportService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.dto.ReportPageDto;

import java.util.List;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Autowired
    private DataSetService dataSetService;

    @Override
    public List<Report> getAllReport(ReportPageDto reportPageDto) {
        return this.list();
    }

    @Override
    public void export(HttpServletResponse response, GenerateReportDto reportDto) throws Exception {
        Report report = this.getById(reportDto.getRtId());
        reportDto.setReportService(report.getReportService());
        reportDto.setReportName(report.getReportName());
        ReportHandleFactory.strategy(report.getReportService()).handleExport(response, reportDto);
    }

    @Override
    public PageEntity getPagePreviewData(ReportPageDto reportPageDto) {
        return dataSetService.getPageData(reportPageDto);
    }

}
