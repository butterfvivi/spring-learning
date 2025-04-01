package org.vivi.framework.ireport.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.model.report.ReportSetting;
import org.vivi.framework.ireport.demo.service.DataSetService;
import org.vivi.framework.ireport.demo.service.ReportDataStrategy;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

import java.util.List;
import java.util.Map;

@Service
public class DataFromMapImpl implements ReportDataStrategy {

    @Autowired
    private DataSetService dataSetService;

    @Override
    public String type() {
        return "dataMap";
    }

    @Override
    public IDynamicExportDto transform(GenerateReportDto reportDto, IDynamicExportDto req) {
        //get dataList
        List<Map<String, Object>> allData = dataSetService.getAllData(reportDto);
        req.setDataList(allData);

        //get headList
        ReportSetting dataset = dataSetService.getById(reportDto.getId());
        String headers = dataset.getDynHeader();
        List<String> headerList = Lists.newArrayList(headers.split(","));
        req.setHeadList(headerList);

        return null;
    }
}
