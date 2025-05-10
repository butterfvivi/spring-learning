package org.vivi.framework.iasync.thread.common.thread.utils;

import com.alibaba.fastjson2.JSONObject;
import org.vivi.framework.iasync.thread.common.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池工具
 */
@EnableAsync
@Configuration
public class ThreadPoolExecutorUtils {
    private static Logger logger = LogManager.getLogger(ThreadPoolExecutorUtils.class);

    private static LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();   // 导出线程队列

    private static Map<String, JSONObject> threadRecordMap = new LinkedHashMap<>();   // 导出线线程记录Map

    /**
     * 采用注解的方式
     **/
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        int i = Runtime.getRuntime().availableProcessors(); // CPU核数 = Runtime.getRuntime().availableProcessors()
        System.out.println("系统最大线程数  ： " + i);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 3, 5000, TimeUnit.SECONDS, blockingQueue, Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /*
     * 获取任务详细信息
     */
    public static JSONObject getDetailInfo() {
        ThreadPoolExecutor instance = (ThreadPoolExecutor) BeanUtils.getBean("taskExecutor");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("任务总数", instance.getTaskCount());
        jsonObject.put("已完成任务", instance.getCompletedTaskCount());
        jsonObject.put("尚未完成任务", instance.getTaskCount() - instance.getCompletedTaskCount());
        jsonObject.put("尚未完成任务信息", threadRecordMap);
        return jsonObject;
    }

    /*
     * 添加/修改 任务信息
     */
    public static void setThreadRecordMap(String key, JSONObject value) {
        synchronized (ThreadPoolSingleton.class) {
            threadRecordMap.put(key, value);
        }
    }
    public static JSONObject getThreadRecordMap(String key) {
        synchronized (ThreadPoolSingleton.class) {
            return threadRecordMap.get(key);
        }
    }

    /*
     * 删除已完成任务信息
     */
    public static void removeThreadRecordMap(String key) {
        synchronized (ThreadPoolSingleton.class) {
            threadRecordMap.remove(key);
        }
    }
    
    /*
     * 获取线程队列单例
     */
    private static LinkedBlockingQueue getInstanceBlockingQueue() {
        if (blockingQueue == null) {
            blockingQueue = new LinkedBlockingQueue<>();
        }
        return blockingQueue;
    }
    
    
    /**
     * 线程池单例
     **/
    public static class ThreadPoolSingleton {

        private static ThreadPoolExecutor instance;  // 导出线程池
        private static LinkedBlockingQueue blockingQueue;   // 导出线程队列
        private static Map<String, JSONObject> threadRecordMap = new LinkedHashMap<>();   // 导出线线程记录Map

        /*
         * 获取任务详细信息
         */
        public static JSONObject getDetailInfo() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("任务总数", getInstance().getTaskCount());
            jsonObject.put("已完成任务", getInstance().getCompletedTaskCount());
            jsonObject.put("尚未完成任务", getInstance().getTaskCount() - getInstance().getCompletedTaskCount());
            jsonObject.put("尚未完成任务信息", threadRecordMap);
            return jsonObject;
        }

        /*
         * 添加/修改 任务信息
         */
        public static void setThreadRecordMap(String key, JSONObject value) {
            synchronized (ThreadPoolSingleton.class) {
                threadRecordMap.put(key, value);
            }
        }
        public static JSONObject getThreadRecordMap(String key) {
            synchronized (ThreadPoolSingleton.class) {
                return threadRecordMap.get(key);
            }
        }

        /*
         * 删除已完成任务信息
         */
        public static void removeThreadRecordMap(String key) {
            synchronized (ThreadPoolSingleton.class) {
                threadRecordMap.remove(key);
            }
        }

        /*
         * 任务进入线程池
         */
        public static void execute(Runnable command) {
            getInstance().execute(command);
        }


        /*
         * 获取线程池单例
         */
        private static ThreadPoolExecutor getInstance() {
            if (instance == null) {
                instance = new ThreadPoolExecutor(2, 3, 5000, TimeUnit.SECONDS, getInstanceBlockingQueue(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
            }
            return instance;
        }
        

        /*
         * 获取线程队列单例
         */
        private static LinkedBlockingQueue getInstanceBlockingQueue() {
            if (blockingQueue == null) {
                blockingQueue = new LinkedBlockingQueue<>();
            }
            return blockingQueue;
        }
        
    }

}