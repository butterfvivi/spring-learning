package org.vivi.framework.ireport.demo.service.report.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.mapper.ReportMapper;
import org.vivi.framework.ireport.demo.model.report.Report;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSet;
import org.vivi.framework.ireport.demo.report.achieve.ExcelInvokeCore;
import org.vivi.framework.ireport.demo.service.datatransform.ReportDataTransformService;
import org.vivi.framework.ireport.demo.service.report.ReportService;
import org.vivi.framework.ireport.demo.service.report.dto.ReportPreviewData;
import org.vivi.framework.ireport.demo.service.report.dto.ReportSheetSetDto;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSetService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.dto.ReportDto;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Autowired
    private ExcelInvokeCore excelInvokeCore;

    @Autowired
    private ReportDataTransformService transformService;

    @Autowired
    private ReportSheetSetService reportSetService;

    @Override
    public List<Report> getAllReport(ReportDto  reportDto) {
        return this.list();
    }

    @Override
    public void export(HttpServletResponse response, GenerateReportDto reportDto) throws Exception {

        //IDynamicExportDto dto = transformService.transform(exportDto);
        getReportSet(reportDto.getId());
        IDynamicExportDto dto = new IDynamicExportDto();
        excelInvokeCore.dynamicExport(response, dto);
    }

    @Override
    public Object getAllPreviewData(GenerateReportDto reportDto) {
        return getReportSet(reportDto.getId());
    }

    public ReportPreviewData getReportSet(Long id) {
        ReportPreviewData reportPreviewData = new ReportPreviewData();
        Report report = this.getBaseMapper().selectById(id);

        reportPreviewData.setReportlName(report.getReportName());

        List<ReportSheetSet> allSheetSetting = reportSetService.getAllSheetSetting(report.getId());
        List<ReportSheetSetDto> sheetSetDtos = new ArrayList<>();

        allSheetSetting.forEach(sheetSetting -> {
            ReportSheetSetDto reportSheetSetDto = new ReportSheetSetDto();
            reportSheetSetDto.setSheetName(sheetSetting.getSheetName());
            reportSheetSetDto.setTitleName(sheetSetting.getTitleName());
            reportSheetSetDto.setSheetOrder(sheetSetting.getSheetOrder());
            reportSheetSetDto.setHeadList(reportSetService.getHeaders(sheetSetting.getId()));
            reportSheetSetDto.setStyleService(sheetSetting.getStyleService());
            sheetSetDtos.add(reportSheetSetDto);
        });

        reportPreviewData.setSheetDatas(sheetSetDtos);
        return reportPreviewData;
    }
}
