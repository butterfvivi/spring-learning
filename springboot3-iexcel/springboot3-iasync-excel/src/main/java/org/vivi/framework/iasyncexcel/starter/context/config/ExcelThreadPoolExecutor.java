package org.vivi.framework.iasyncexcel.starter.context.config;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池工具类
 */
@EnableAsync
@Configuration
@Slf4j
public class ExcelThreadPoolExecutor {

    // 线程池配置参数
    private static int CORE_POOL_SIZE = 2;
    private static int MAX_POOL_SIZE = 3;
    private static final int QUEUE_CAPACITY = 1000;
    private static final int KEEP_ALIVE_SECONDS = 5000;
    private static final String THREAD_NAME_PREFIX = "data-pool";

    /**
     * 创建并配置线程池
     */
    @Bean("taskExecutor")
    @ConditionalOnMissingBean
    public ThreadPoolExecutor taskExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        if (processors > 1){
            CORE_POOL_SIZE = 2 * processors-1;
            MAX_POOL_SIZE = 4 * processors-1;
        }
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(20)
        );
    }

    /**
     * 线程池管理类
     */
    public static class ThreadPoolManager {
        private static final ThreadPoolTaskExecutor INSTANCE = initializeExecutor();
        private static final Map<String, CompletableFuture> THREAD_RECORD_MAP = new LinkedHashMap<>();

        /**
         * 初始化线程池
         */
        private static ThreadPoolTaskExecutor initializeExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(CORE_POOL_SIZE);
            executor.setMaxPoolSize(MAX_POOL_SIZE);
            executor.setQueueCapacity(QUEUE_CAPACITY);
            executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
            executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.initialize();
            return executor;
        }

        /**
         * 获取线程池实例
         */
        public static ThreadPoolTaskExecutor getInstance() {
            return INSTANCE;
        }

        /**
         * 执行任务
         */
        public static void execute(Runnable runnable) {
            getInstance().execute(runnable);
        }

        /**
         * 获取任务详细信息
         */
        public static JSONObject getDetailInfo() {
            JSONObject jsonObject = new JSONObject();
            ThreadPoolExecutor executor = getInstance().getThreadPoolExecutor();
            jsonObject.put("任务总数", executor.getTaskCount());
            jsonObject.put("已完成任务", executor.getCompletedTaskCount());
            jsonObject.put("尚未完成任务", executor.getTaskCount() - executor.getCompletedTaskCount());
            jsonObject.put("尚未完成任务信息", THREAD_RECORD_MAP);
            return jsonObject;
        }

        /**
         * 添加/修改任务信息
         */
        public static void addThreadRecord(String key, CompletableFuture threadTask) {
            synchronized (ThreadPoolManager.class) {
                THREAD_RECORD_MAP.put(key, threadTask);
            }
        }

        /**
         * 删除已完成任务信息
         */
        public static void removeThreadRecord(String key) {
            synchronized (ThreadPoolManager.class) {
                THREAD_RECORD_MAP.remove(key);
            }
        }

        /**
         * 获取线程队列
         */
        public static BlockingQueue<Runnable> getTaskQueue() {
            return getInstance().getThreadPoolExecutor().getQueue();
        }
    }

}