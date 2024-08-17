package org.vivi.framework.iasyncexcel.starter.context.config;

import java.util.concurrent.ExecutorService;

public class ExcelThreadPool {

    private ExecutorService executor;

    public ExcelThreadPool(ExecutorService executor) {
        this.executor = executor;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
