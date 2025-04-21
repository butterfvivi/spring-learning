package org.vivi.framework.report.bigdata.utils.dp.strategy;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.vivi.framework.report.bigdata.paging.dp.strategy.XStrategy;


@Slf4j
public class XStrategyTest {

    @Test
    public void test() throws IllegalArgumentException {
        //Assert.equals(XStrategy.strategy(X_STRATEGY_TYPE_TEST1).handler("1"), "1");
        //Assert.equals(XStrategy.strategy(X_STRATEGY_TYPE_TEST2).handler("2"), "2");
        try {
            Assert.equals(XStrategy.strategy("other").handler("3"), "3");
        } catch (IllegalArgumentException e) {
            log.error("参数异常！", e);
            Assert.isTrue(e.getMessage().contains("not found"));
        }
    }
}
