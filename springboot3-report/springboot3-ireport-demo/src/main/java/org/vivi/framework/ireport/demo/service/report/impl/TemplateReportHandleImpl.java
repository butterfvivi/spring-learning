package org.vivi.framework.ireport.demo.service.report.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.utils.AssertUtils;
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

import java.util.List;

import static org.vivi.framework.ireport.demo.common.constant.Message.NO_FILE_PATH;

@Service
public class TemplateReportHandleImpl implements ReportHandleStrategy {

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ReportSheetSetService reportSheetSetService;

    @Autowired
    private ReportDataTransformService reportDataTransformService;

    private static ExcelInvokeCore ExcelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);
    @Override
    public String target() {
        return "template";
    }

    @Override
    public void handleExport(HttpServletResponse response, GenerateReportDto reportDto) {
        //获取所有report的所有数据集
        List<ReportDataSet> allReportDataSet = dataSetService.getAllReportSet(reportDto.getRtId());

        String templatePath = reportDto.getTemplateSet();
        AssertUtils.objIsNull(templatePath, NO_FILE_PATH);

        allReportDataSet.forEach(reportDataSet -> {
            ReportSheetSetDto reportSheetSetDto = new ReportSheetSetDto();
            //获取数据集的sheet信息
            ReportSheetSet reportSheetSet = reportSheetSetService.getReportSheetSetById(reportDataSet.getSheetIndex());

            DataSearchDto dataSearchDto = new DataSearchDto();
            dataSearchDto.setSetId(reportDataSet.getId());
            dataSearchDto.setParams(reportDto.getSearchData());

            //数据转换
            List sheetDatas = reportDataTransformService.transform(dataSearchDto);
            reportSheetSetDto.setCellDatas(sheetDatas);
        });
        //execute excel export
        //IExcelUtils.writerTemplateToWeb(response, dataList, templatePath, otherValMap, config);
    }

}
