package org.vivi.framework.ireport.demo.service.report.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.exception.BizException;
import org.vivi.framework.ireport.demo.model.dataset.ReportDataSet;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSet;
import org.vivi.framework.ireport.demo.report.achieve.ExcelInvokeCore;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.datatransform.ReportDataTransformService;
import org.vivi.framework.ireport.demo.service.report.ReportHandleStrategy;
import org.vivi.framework.ireport.demo.service.report.dto.ReportSheetSetDto;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSetService;
import org.vivi.framework.ireport.demo.web.dto.DataSearchDto;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicReportHandleImpl implements ReportHandleStrategy {

    @Autowired
    private ReportSheetSetService reportSheetSetService;

    @Autowired
    private ExcelInvokeCore  excelInvokeCore;

    @Autowired
    private ReportDataTransformService reportDataTransformService;

    @Autowired
    private DataSetService dataSetService;

    public static final String DYNAMC_REPORT_STRATEGY = "dynamic";

    @Override
    public String target() {
        return DYNAMC_REPORT_STRATEGY;
    }

    @Override
    public void handleExport(HttpServletResponse response, GenerateReportDto reportDto) {
        try {
            GenerateReportDto newPreviewDto = new GenerateReportDto();

            List<ReportSheetSetDto> sheetSetDtos = new ArrayList<>();

            List<ReportDataSet> allReportSet = dataSetService.getAllReportSet(reportDto.getRtId());
            allReportSet.forEach(reportDataSet -> {
                ReportSheetSetDto reportSheetSetDto = new ReportSheetSetDto();
                ReportSheetSet reportSheetSet = reportSheetSetService.getReportSheetSetById(reportDataSet.getSheetIndex());

                reportSheetSetDto.setSheetName(reportSheetSet.getSheetName());
                reportSheetSetDto.setTitleName(reportSheetSet.getTitleName());
                reportSheetSetDto.setReportSqls(reportDataSet.getRtSql());
                reportSheetSetDto.setSheetOrder(reportSheetSet.getSheetOrder());

                reportSheetSetDto.setHeadList(reportSheetSetService.getHeaders(reportDataSet.getId()));

                DataSearchDto dataSearchDto = new DataSearchDto();
                dataSearchDto.setSetId(reportDataSet.getId());
                dataSearchDto.setParams(reportDto.getSearchData());

                List sheetDatas = reportDataTransformService.transform(dataSearchDto);
                reportSheetSetDto.setCellDatas(sheetDatas);

                sheetSetDtos.add(reportSheetSetDto);
            });

            newPreviewDto.setReportName(reportDto.getReportName());
            newPreviewDto.setSearchData(reportDto.getSearchData());
            newPreviewDto.setSheetDatas(sheetSetDtos);

            excelInvokeCore.dynamicExport(response, newPreviewDto);
        }catch (Exception e){
            throw new BizException("500", "单元格数据解析失败，请检查单元格数据格式是否正确！");
        }
    }


}
