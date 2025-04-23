package org.vivi.framework.ireport.demo.service.datatransform.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSet;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.datatransform.IReportDataStrategy;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSetService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

import java.util.List;
import java.util.Map;

@Service
public class DataFromMapStrategyI implements IReportDataStrategy {

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ReportSheetSetService reportSheetSetService;

    public static final String X_STRATEGY_TYPE_TEST2 = "dataMap";
    @Override
    public String type() {
        return X_STRATEGY_TYPE_TEST2;
    }

    @Override
    public IDynamicExportDto transform(IDynamicExportDto req) {
        GenerateReportDto reportDto = req.getReportDto();
        //get dataList
        List<Map<String, Object>> allData = dataSetService.getAllData(reportDto);
        req.setDataList(allData);

        //get headList
        ReportSheetSet dataset = reportSheetSetService.getById(reportDto.getId());
        String headers = dataset.getDynHeader();
        List<String> headerList = Lists.newArrayList(headers.split(","));
        req.setHeadList(headerList);

        return req;
    }
}
