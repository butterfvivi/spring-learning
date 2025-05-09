package org.vivi.framework.ireport.demo.service.datatransform.converter;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.service.datatransform.ReportDataTransformService;
import org.vivi.framework.ireport.demo.web.dto.DataSearchDto;

import java.util.List;

@Service
public class ObjectDataHandleConverter  implements ReportDataTransformService {

    @Override
    public List<T> transform(DataSearchDto dataSearchDto) {
        return List.of();
    }
}
