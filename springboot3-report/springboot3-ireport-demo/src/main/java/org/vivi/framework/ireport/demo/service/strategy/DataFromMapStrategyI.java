package org.vivi.framework.ireport.demo.service.strategy;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSetting;
import org.vivi.framework.ireport.demo.service.ReportDataSetService;
import org.vivi.framework.ireport.demo.service.IReportDataStrategy;
import org.vivi.framework.ireport.demo.service.ReportSheetSettingService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

import java.util.List;
import java.util.Map;

@Service
public class DataFromMapStrategyI implements IReportDataStrategy {

    @Autowired
    private ReportDataSetService reportDataSetService;

    @Autowired
    private ReportSheetSettingService  reportSheetSettingService;

    public static final String X_STRATEGY_TYPE_TEST2 = "dataMap";
    @Override
    public String type() {
        return X_STRATEGY_TYPE_TEST2;
    }

    @Override
    public IDynamicExportDto transform(IDynamicExportDto req) {
        GenerateReportDto reportDto = req.getReportDto();
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
