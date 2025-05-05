package org.vivi.framework.ireport.demo.service.report.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.utils.AssertUtils;
import org.vivi.framework.ireport.demo.common.utils.IocUtil;
import org.vivi.framework.ireport.demo.model.dataset.ReportDataSet;
import org.vivi.framework.ireport.demo.report.achieve.ExcelInvokeCore;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.report.ReportHandleStrategy;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.List;

import static org.vivi.framework.ireport.demo.common.constant.Message.NO_FILE_PATH;

@Service
public class TemplateReportHandleImpl implements ReportHandleStrategy {

    @Autowired
    private DataSetService dataSetService;

    private static ExcelInvokeCore ExcelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);
    @Override
    public String target() {
        return "template";
    }

    @Override
    public void handleExport(HttpServletResponse response, GenerateReportDto reportDto) {
        List<ReportDataSet> allReportSet = dataSetService.getAllReportSet(reportDto.getRtId());

        String templatePath = reportDto.getTemplateSet();
        AssertUtils.objIsNull(templatePath, NO_FILE_PATH);

        allReportSet.forEach(reportDataSet -> {

        });
        //execute excel export
        //IExcelUtils.writerTemplateToWeb(response, dataList, templatePath, otherValMap, config);
    }

}
