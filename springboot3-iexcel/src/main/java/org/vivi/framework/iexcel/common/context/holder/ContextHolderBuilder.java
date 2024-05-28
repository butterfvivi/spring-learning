package org.vivi.framework.iexcel.common.context.holder;


import org.vivi.framework.iexcel.common.context.Context;
import org.vivi.framework.iexcel.common.support.ExcelCommonException;


public class ContextHolderBuilder<T extends Context> {

    private Class<? extends ContextHolder<String, T>> strategy;

    public static <T extends Context> ContextHolderBuilder<T> create() {
        return new ContextHolderBuilder<>();
    }

    public ContextHolderBuilder<T> strategy(Class<? extends ContextHolder<String, T>> strategy) {
        this.strategy = strategy;
        return this;
    }

    public ContextHolder<String, T> build() {
        if (strategy == null) {
            return new LocalContextHolder<>();
        }
        try {
            return strategy.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ExcelCommonException(e);
        }
    }
}
