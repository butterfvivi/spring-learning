package org.vivi.framework.report.bigdata.service.impl;

import org.springframework.stereotype.Service;
import org.vivi.framework.report.bigdata.entity.Cinfo;
import org.vivi.framework.report.bigdata.mapper.CinfoMapper;
import org.vivi.framework.report.bigdata.paging.service.BaseServiceImpl;
import org.vivi.framework.report.bigdata.service.CinfoService;


/**
 * 演示服务impl
 */
@Service
public class CinfoServiceImpl extends BaseServiceImpl<CinfoMapper, Cinfo> implements CinfoService {
}
