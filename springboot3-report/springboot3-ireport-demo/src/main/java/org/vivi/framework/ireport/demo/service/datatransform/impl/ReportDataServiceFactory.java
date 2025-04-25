package org.vivi.framework.ireport.demo.service.datatransform.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.datatransform.IReportDataStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 策略模式
 */
@Slf4j
@Component
public class ReportDataServiceFactory  implements InitializingBean, ApplicationContextAware {

    private static final Map<String, IReportDataStrategy> REPORTDATA_TRATEGY_MAP = new ConcurrentHashMap<>();

    @Autowired
    private DataSetService dataSetService;

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        Map<String, IReportDataStrategy> beanMap = applicationContext.getBeansOfType(IReportDataStrategy.class);
        //遍历该接口的所有实现，将其放入map中
        for (IReportDataStrategy serviceImpl : beanMap.values()) {
            REPORTDATA_TRATEGY_MAP.put(serviceImpl.type(), serviceImpl);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    static {
        long startTime = System.currentTimeMillis();
        ServiceLoader.load(IReportDataStrategy.class).forEach(xStrategyInterface -> {
            log.info("注册XStrategy策略：{} -> {}", xStrategyInterface.type(), xStrategyInterface.getClass().getName());
            register(xStrategyInterface);
        });
        log.info("XStrategy初始化用时:{}ms", System.currentTimeMillis() - startTime);
    }

    private static void register(IReportDataStrategy strategyInterface) {
        REPORTDATA_TRATEGY_MAP.put(strategyInterface.type(), strategyInterface);
    }

    public static IReportDataStrategy strategy(String strategyType) {
        return Optional.ofNullable(REPORTDATA_TRATEGY_MAP.get(strategyType))
                .orElseThrow(() -> new IllegalArgumentException("ReportDataStrategy->type:$strategyType not found"));
    }
//
//    @Override
//    public List transform(GenerateReportDto previewDto) {
//        if (previewDto == null) {
//            throw new BizException("402", "注册的类缺少参数");
//        }
//
//        Report report = dataSetService.getById(previewDto.getRtId());
//
//        return strategy(report.getReportService()).transform(previewDto);
//    }

}