package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IReportDatasourceDictDataService;
import org.vivi.framework.report.service.mapper.reportdatasourcedictdata.ReportDatasourceDictDataMapper;
import org.vivi.framework.report.service.model.reportdatasourcedictdata.ReportDatasourceDictData;

@Service
public class ReportDatasourceDictDataServiceImpl extends ServiceImpl<ReportDatasourceDictDataMapper, ReportDatasourceDictData> implements IReportDatasourceDictDataService {
}
