package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IReportRangeAuthService;
import org.vivi.framework.report.service.mapper.reportrangeauth.ReportRangeAuthMapper;
import org.vivi.framework.report.service.model.reportrangeauth.ReportRangeAuth;

@Service
public class ReportRangeAuthServiceImpl extends ServiceImpl<ReportRangeAuthMapper, ReportRangeAuth> implements IReportRangeAuthService {
}
