package org.vivi.framework.report.bigdata.service.impl;

import org.springframework.stereotype.Service;
import org.vivi.framework.report.bigdata.entity.Demo;
import org.vivi.framework.report.bigdata.mapper.DemoMapper;
import org.vivi.framework.report.bigdata.service.DemoService;


/**
 * 演示服务impl
 */
@Service
public class DemoServiceImpl extends BaseServiceImpl<DemoMapper, Demo> implements DemoService {
}
