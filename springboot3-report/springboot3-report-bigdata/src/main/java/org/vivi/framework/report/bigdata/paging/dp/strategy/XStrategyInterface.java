package org.vivi.framework.report.bigdata.paging.dp.strategy;


/**
 * 策略模式接口
 */
public interface XStrategyInterface {
    /**
     * 类型
     *
     * @return {@link String}
     */
    String type();

    /**
     * 处理程序
     *
     * @param param 参数
     * @return {@link Object}
     */
    Object handler(Object param);
}
