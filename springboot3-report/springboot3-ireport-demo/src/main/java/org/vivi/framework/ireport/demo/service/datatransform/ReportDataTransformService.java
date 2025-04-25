package org.vivi.framework.ireport.demo.service.datatransform;

import org.apache.poi.ss.formula.functions.T;
import org.vivi.framework.ireport.demo.web.dto.DataSearchDto;

import java.util.List;

public interface ReportDataTransformService {

    List<T> transform(DataSearchDto dataSearchDto);

}
