package org.vivi.framework.ireport.demo.service.datasettransform.impl;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.ireport.demo.service.datasettransform.IReportDataStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 策略模式
 */
@Slf4j
@UtilityClass
public class ReportDataServiceFactory   {

    private static final Map<String, IReportDataStrategy> XSTRATEGY_HANDLER_MAP = new ConcurrentHashMap<>();

    static {
        long startTime = System.currentTimeMillis();
        ServiceLoader.load(IReportDataStrategy.class).forEach(xStrategyInterface -> {
            log.info("注册XStrategy策略：{} -> {}", xStrategyInterface.type(), xStrategyInterface.getClass().getName());
            register(xStrategyInterface);
        });
        log.info("XStrategy初始化用时:{}ms", System.currentTimeMillis() - startTime);
    }

    private static void register(IReportDataStrategy strategyInterface) {
        XSTRATEGY_HANDLER_MAP.put(strategyInterface.type(), strategyInterface);
    }

    public static IReportDataStrategy strategy(String strategyType) {
        return Optional.ofNullable(XSTRATEGY_HANDLER_MAP.get(strategyType))
                .orElseThrow(() -> new IllegalArgumentException("ReportDataStrategy->type:$strategyType not found"));
    }

}