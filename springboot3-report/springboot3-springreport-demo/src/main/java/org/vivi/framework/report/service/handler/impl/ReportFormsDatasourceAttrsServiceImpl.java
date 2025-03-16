package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IReportFormsDatasourceAttrsService;
import org.vivi.framework.report.service.mapper.reportformsdatasourceattrs.ReportFormsDatasourceAttrsMapper;
import org.vivi.framework.report.service.model.reportformsdatasourceattrs.ReportFormsDatasourceAttrs;

@Service
public class ReportFormsDatasourceAttrsServiceImpl  extends ServiceImpl<ReportFormsDatasourceAttrsMapper, ReportFormsDatasourceAttrs> implements IReportFormsDatasourceAttrsService {
}
