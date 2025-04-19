package org.vivi.framework.ireport.demo.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSetting;
import org.vivi.framework.ireport.demo.service.ReportDataSetService;
import org.vivi.framework.ireport.demo.service.ReportDataStrategy;
import org.vivi.framework.ireport.demo.service.ReportSheetSettingService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

import java.util.List;
import java.util.Map;

@Service
public class DataFromMapImpl implements ReportDataStrategy {

    @Autowired
    private ReportDataSetService reportDataSetService;

    @Autowired
    private ReportSheetSettingService  reportSheetSettingService;

    @Override
    public String type() {
        return "dataMap";
    }

    @Override
    public IDynamicExportDto transform(GenerateReportDto reportDto, IDynamicExportDto req) {
        //get dataList
        List<Map<String, Object>> allData = reportDataSetService.getAllData(reportDto);
        req.setDataList(allData);

        //get headList
        ReportSheetSetting dataset = reportSheetSettingService.getById(reportDto.getId());
        String headers = dataset.getDynHeader();
        List<String> headerList = Lists.newArrayList(headers.split(","));
        req.setHeadList(headerList);

        return req;
    }
}
