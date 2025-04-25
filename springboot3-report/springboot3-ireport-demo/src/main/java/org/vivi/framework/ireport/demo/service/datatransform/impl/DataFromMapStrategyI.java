package org.vivi.framework.ireport.demo.service.datatransform.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.datatransform.IReportDataStrategy;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.List;
import java.util.Map;

@Service
public class DataFromMapStrategyI implements IReportDataStrategy {

    @Autowired
    private DataSetService dataSetService;

    public static final String MAPDATA_STRATEGY_TYPE = "dataMap";
    @Override
    public String type() {
        return MAPDATA_STRATEGY_TYPE;
    }

    @Override
    public List<Map<String, Object>> transform(GenerateReportDto dto) {
        //get dataList
        return dataSetService.getAllMapData(dto);
    }
}
