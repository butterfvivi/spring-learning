package org.vivi.framework.ireport.demo.service.reportset.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.mapper.ReportMapper;
import org.vivi.framework.ireport.demo.model.report.Report;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSetting;
import org.vivi.framework.ireport.demo.service.reportset.ReportSetService;
import org.vivi.framework.ireport.demo.service.reportset.dto.ReportPreviewData;
import org.vivi.framework.ireport.demo.service.reportset.dto.ReportSheetSetDto;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSettingService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportSetServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportSetService {


    @Autowired
    private ReportSheetSettingService  reportSheetSettingService;

    @Override
    public ReportPreviewData getReportSet(Integer id) {
        ReportPreviewData reportPreviewData = new ReportPreviewData();
        Report report = this.getBaseMapper().selectById(id);

        reportPreviewData.setReportlName(report.getReportName());

        List<ReportSheetSetting> allSheetSetting = reportSheetSettingService.getAllSheetSetting(report.getId());
        List<ReportSheetSetDto> sheetSetDtos = new ArrayList<>();

        allSheetSetting.forEach(sheetSetting -> {
            ReportSheetSetDto reportSheetSetDto = new ReportSheetSetDto();
            reportSheetSetDto.setSheetName(sheetSetting.getSheetName());
            reportSheetSetDto.setTitleName(sheetSetting.getTitleName());
            reportSheetSetDto.setSheetOrder(sheetSetting.getSheetOrder());
            reportSheetSetDto.setDynHeader(sheetSetting.getDynHeader());
            reportSheetSetDto.setStyleService(sheetSetting.getStyleService());
            sheetSetDtos.add(reportSheetSetDto);
        });

        reportPreviewData.setSheetDatas(sheetSetDtos);
        return reportPreviewData;
    }
}
