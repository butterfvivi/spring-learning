package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IReportFormsDatasourceService;
import org.vivi.framework.report.service.mapper.reportformsdatasource.ReportFormsDatasourceMapper;
import org.vivi.framework.report.service.model.reportformsdatasource.ReportFormsDatasource;

@Service
public class ReportFormsDatasourceServiceImpl extends ServiceImpl<ReportFormsDatasourceMapper, ReportFormsDatasource> implements IReportFormsDatasourceService {
}
