package org.vivi.framework.ireport.demo.service.datatransform;

import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

public interface ReportDataTransformService {

    IDynamicExportDto transform(IDynamicExportDto req);

}
