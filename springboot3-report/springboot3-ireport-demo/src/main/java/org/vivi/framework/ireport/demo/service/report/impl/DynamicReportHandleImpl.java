package org.vivi.framework.ireport.demo.service.report.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSet;
import org.vivi.framework.ireport.demo.report.achieve.ExcelInvokeCore;
import org.vivi.framework.ireport.demo.service.datatransform.impl.ReportDataServiceFactory;
import org.vivi.framework.ireport.demo.service.report.ReportHandleStrategy;
import org.vivi.framework.ireport.demo.service.report.dto.ReportSheetSetDto;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSetService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicReportHandleImpl implements ReportHandleStrategy {

    @Autowired
    private ReportSheetSetService reportSetService;

    @Autowired
    private ExcelInvokeCore  excelInvokeCore;

    public static final String DYNAMC_REPORT_STRATEGY = "dynamic";

    @Override
    public String target() {
        return DYNAMC_REPORT_STRATEGY;
    }

    @Override
    public void handleExport(HttpServletResponse response, GenerateReportDto reportDto) {
        try {
            GenerateReportDto newPreviewDto = new GenerateReportDto();

            List<ReportSheetSet> allSheetSet = reportSetService.getAllSheetSet(reportDto.getRtId());
            List<ReportSheetSetDto> sheetSetDtos = new ArrayList<>();

            allSheetSet.forEach(sheetSetting -> {
                ReportSheetSetDto reportSheetSetDto = new ReportSheetSetDto();
                reportSheetSetDto.setSheetName(sheetSetting.getSheetName());
                reportSheetSetDto.setTitleName(sheetSetting.getTitleName());
                reportSheetSetDto.setSheetOrder(sheetSetting.getSheetOrder());
                reportSheetSetDto.setReportSqls(sheetSetting.getCalFormula());
                reportSheetSetDto.setHeadList(reportSetService.getHeaders(sheetSetting.getId()));

                List sheetDatas = ReportDataServiceFactory.strategy("dataMap").transform(reportDto);
                reportSheetSetDto.setCellDatas(sheetDatas);

                sheetSetDtos.add(reportSheetSetDto);
            });

            newPreviewDto.setReportName(reportDto.getReportName());
            newPreviewDto.setSearchData(reportDto.getSearchData());
            newPreviewDto.setSheetDatas(sheetSetDtos);

            excelInvokeCore.dynamicExport(response, newPreviewDto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
