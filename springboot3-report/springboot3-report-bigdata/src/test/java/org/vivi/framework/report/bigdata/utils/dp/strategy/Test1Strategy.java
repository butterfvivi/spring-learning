package org.vivi.framework.report.bigdata.utils.dp.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vivi.framework.report.bigdata.paging.dp.strategy.XStrategyInterface;

@Slf4j
@Component
public class Test1Strategy implements XStrategyInterface {
    public static final String X_STRATEGY_TYPE_TEST1 = "test1";
    @Override
    public String type() {
        return X_STRATEGY_TYPE_TEST1;
    }

    @Override
    public Object handler(Object param) {
        log.info("param:{}", param);
        return param;
    }
}
