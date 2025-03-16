package org.vivi.framework.report.service.core.calculate;

/**
 * <p>Title: Calculate</p>
 * <p>Description: 计算数据</p>
 */
public abstract class Calculate<T> {

    /**
     *<p>Title: calculate</p>
     *<p>Description: 计算数据</p>
     */
    public abstract String calculate(T bindData);
}
