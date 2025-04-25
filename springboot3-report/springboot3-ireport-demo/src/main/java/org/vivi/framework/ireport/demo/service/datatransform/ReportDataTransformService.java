package org.vivi.framework.ireport.demo.service.datatransform;

import org.vivi.framework.ireport.demo.service.report.dto.ReportSheetSetDto;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

public interface ReportDataTransformService {

    ReportSheetSetDto transform(GenerateReportDto req);

}
