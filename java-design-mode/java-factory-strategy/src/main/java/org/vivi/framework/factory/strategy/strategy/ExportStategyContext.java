package org.vivi.framework.factory.strategy.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略模式
 */
@Component
public class ExportStategyContext {

    private final Map<String,ExportService> strategyMap = new ConcurrentHashMap<>();

    /**
     * 构造函数，把spring容器中所有关于该接口的子类，全部放入到集合中
     * @param payStrategyList
     */
    public ExportStategyContext(List<ExportService> payStrategyList) {
        for (ExportService exportService : payStrategyList) {
            strategyMap.put(exportService.getMode(), exportService);
        }
    }


    public Object useStrategy(String code) {
        ExportService strategy = this.strategyMap.get(code);
        if (strategy == null) {
            throw new RuntimeException("error");
        }
        return strategy.exportData();
    }

}
