package org.vivi.framework.report.bigdata.utils.dp.strategy;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.report.bigdata.paging.dp.strategy.XStrategyInterface;

@Slf4j
@AutoService(XStrategyInterface.class)
public class Test2Strategy implements XStrategyInterface {
    public static final String X_STRATEGY_TYPE_TEST2 = "test2";
    @Override
    public String type() {
        return X_STRATEGY_TYPE_TEST2;
    }

    @Override
    public Object handler(Object param) {
        log.info("param:{}", param);
        return param;
    }
}
