package org.vivi.framework.iasyncexcel.starter.context.config;

import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExcelThreadPool implements DisposableBean, AutoCloseable {

    private final ExecutorService executor;

    public ExcelThreadPool(ExecutorService executor) {
        this.executor = executor;

        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Get statistics about the thread pool if available
     * @return Statistics string or "Not available" if not a ThreadPoolExecutor
     */
    public String getPoolStats() {
        if (executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) executor;
            return String.format(
                "Pool stats: active=%d, completed=%d, queue size=%d, core size=%d, max size=%d",
                tpe.getActiveCount(),
                tpe.getCompletedTaskCount(),
                tpe.getQueue().size(),
                tpe.getCorePoolSize(),
                tpe.getMaximumPoolSize()
            );
        }
        return "Pool stats not available";
    }

    @Override
    public void destroy() {
        shutdown();
    }

    @Override
    public void close() {
        shutdown();
    }

    private void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println("Thread pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
