package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IReportTplDatasourceService;
import org.vivi.framework.report.service.mapper.reporttpldatasource.ReportTplDatasourceMapper;
import org.vivi.framework.report.service.model.reporttpldatasource.ReportTplDatasource;

@Service
public class ReportTplDatasourceServiceImpl  extends ServiceImpl<ReportTplDatasourceMapper, ReportTplDatasource> implements IReportTplDatasourceService {
}
