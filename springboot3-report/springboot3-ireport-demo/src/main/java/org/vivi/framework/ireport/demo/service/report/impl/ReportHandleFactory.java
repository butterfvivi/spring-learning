package org.vivi.framework.ireport.demo.service.report.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.vivi.framework.ireport.demo.service.report.ReportHandleStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ReportHandleFactory implements InitializingBean, ApplicationContextAware {

    private static final Map<String, ReportHandleStrategy> REPORT_STRATEGY_HANDLER_MAP = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        Map<String, ReportHandleStrategy> beanMap = applicationContext.getBeansOfType(ReportHandleStrategy.class);
        //遍历该接口的所有实现，将其放入map中
        for (ReportHandleStrategy serviceImpl : beanMap.values()) {
            REPORT_STRATEGY_HANDLER_MAP.put(serviceImpl.target(), serviceImpl);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    static {
        long startTime = System.currentTimeMillis();
        ServiceLoader.load(ReportHandleStrategy.class).forEach(xStrategyInterface -> {
            log.info("注册XStrategy策略：{} -> {}", xStrategyInterface.target(), xStrategyInterface.getClass().getName());
            register(xStrategyInterface);
        });
        log.info("XStrategy初始化用时:{}ms", System.currentTimeMillis() - startTime);
    }

    private static void register(ReportHandleStrategy strategyInterface) {
        REPORT_STRATEGY_HANDLER_MAP.put(strategyInterface.target(), strategyInterface);
    }

    public static ReportHandleStrategy strategy(String strategyType) {
        return Optional.ofNullable(REPORT_STRATEGY_HANDLER_MAP.get(strategyType))
                .orElseThrow(() -> new IllegalArgumentException("ReportDataStrategy->type:$strategyType not found"));
    }

}