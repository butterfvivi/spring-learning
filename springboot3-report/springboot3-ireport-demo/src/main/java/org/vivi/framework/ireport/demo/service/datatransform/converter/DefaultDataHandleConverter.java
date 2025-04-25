package org.vivi.framework.ireport.demo.service.datatransform.converter;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.datatransform.ReportDataTransformService;
import org.vivi.framework.ireport.demo.web.dto.DataSearchDto;

import java.util.List;

@Service
public class DefaultDataHandleConverter implements ReportDataTransformService {

    @Autowired
    private DataSetService dataSetService;

    @Override
    public List<T> transform(DataSearchDto searchDto) {
        List mapDataList = dataSetService.getAllMapData(searchDto);

        return mapDataList;
    }
}
