package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IReportRangeAuthUserService;
import org.vivi.framework.report.service.mapper.reportrangeauthuser.ReportRangeAuthUserMapper;
import org.vivi.framework.report.service.model.reportrangeauthuser.ReportRangeAuthUser;

@Service
public class ReportRangeAuthUserServiceImpl extends ServiceImpl<ReportRangeAuthUserMapper, ReportRangeAuthUser> implements IReportRangeAuthUserService {
}
