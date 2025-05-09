package org.vivi.framework.ireport.demo.service.report.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.exception.BizException;
import org.vivi.framework.ireport.demo.common.utils.IocUtil;
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
    private ReportDataTransformService reportDataTransformService;

    @Autowired
    private DataSetService dataSetService;

    private static ExcelInvokeCore ExcelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);

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

            //获取所有report的所有数据集
            List<ReportDataSet> allReportDataSet = dataSetService.getAllReportSet(reportDto.getRtId());
            //获取所有report的数据集，并做数据转换
            allReportDataSet.forEach(reportDataSet -> {
                ReportSheetSetDto reportSheetSetDto = new ReportSheetSetDto();
                //获取数据集的sheet信息
                ReportSheetSet reportSheetSet = reportSheetSetService.getReportSheetSetById(reportDataSet.getSheetIndex());

                reportSheetSetDto.setSheetName(reportSheetSet.getSheetName());
                reportSheetSetDto.setTitleName(reportSheetSet.getTitleName());
                reportSheetSetDto.setReportSqls(reportDataSet.getRtSql());
                reportSheetSetDto.setSheetOrder(reportSheetSet.getSheetOrder());

                //获取每个sheet的headers
                reportSheetSetDto.setHeadList(reportSheetSetService.getHeaders(reportDataSet.getId()));

                DataSearchDto dataSearchDto = new DataSearchDto();
                dataSearchDto.setSetId(reportDataSet.getId());
                dataSearchDto.setParams(reportDto.getSearchData());

                //数据转换
                List sheetDatas = reportDataTransformService.transform(dataSearchDto);
                reportSheetSetDto.setCellDatas(sheetDatas);

                sheetSetDtos.add(reportSheetSetDto);
            });

            newPreviewDto.setReportName(reportDto.getReportName());
            newPreviewDto.setSearchData(reportDto.getSearchData());
            newPreviewDto.setSheetDatas(sheetSetDtos);

            //设置数据后导出excel
            ExcelInvokeCore.dynamicExport(response, newPreviewDto);
        }catch (Exception e){
            throw new BizException("500", "单元格数据解析失败，请检查单元格数据格式是否正确！");
        }
    }


}
