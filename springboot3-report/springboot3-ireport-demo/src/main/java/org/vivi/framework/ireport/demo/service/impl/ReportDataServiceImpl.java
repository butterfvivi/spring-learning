package org.vivi.framework.ireport.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.vivi.framework.ireport.demo.common.exception.BizException;
import org.vivi.framework.ireport.demo.mapper.ReportMapper;
import org.vivi.framework.ireport.demo.model.report.Report;
import org.vivi.framework.ireport.demo.service.IReportDataStrategy;
import org.vivi.framework.ireport.demo.service.ReportDataTransformService;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ReportDataServiceImpl implements ReportDataTransformService, InitializingBean, ApplicationContextAware {

    private Map<String, IReportDataStrategy> queryServiceImplMap = new HashMap<>();

    private ApplicationContext applicationContext;

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public void afterPropertiesSet() {
        Map<String, IReportDataStrategy> beanMap = applicationContext.getBeansOfType(IReportDataStrategy.class);
        //遍历该接口的所有实现，将其放入map中
        for (IReportDataStrategy serviceImpl : beanMap.values()) {
            queryServiceImplMap.put(serviceImpl.type(), serviceImpl);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public IReportDataStrategy getTarget(String type) {
        for (String s : queryServiceImplMap.keySet()) {
            if (s.contains(type)) {
                return queryServiceImplMap.get(s);
            }
        }
        throw new BizException("404", "未找到对应的处理类");
    }

    @Override
    public IDynamicExportDto transform(IDynamicExportDto exportDto) {
        if (exportDto == null || exportDto.getReportDto() == null) {
            throw new BizException("402", "注册的类缺少参数");
        }

        Report report = reportMapper.selectById(exportDto.getReportDto().getId());

        return getTarget(report.getRtStrategy()).transform(exportDto);
    }
}

