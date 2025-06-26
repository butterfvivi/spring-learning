package org.vivi.framework.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis分布式锁配置属性
 */
@Data
@ConfigurationProperties(prefix = "vivi.redis-lock")
public class RedisLockProperties {
    
    /**
     * 是否启用分布式锁
     */
    private boolean enabled = true;
    
    /**
     * Redis配置
     */
    private Redis redis = new Redis();
    
    /**
     * 监控配置
     */
    private Monitor monitor = new Monitor();
    
    /**
     * 健康检查配置
     */
    private HealthCheck healthCheck = new HealthCheck();
    
    @Data
    public static class Redis {
        /**
         * 锁键前缀
         */
        private String prefix = "distributed-lock:";
        
        /**
         * 默认过期时间（毫秒）
         */
        private long defaultExpireTime = 30000;
        
        /**
         * 重试次数
         */
        private int retryTimes = 3;
        
        /**
         * 重试间隔（毫秒）
         */
        private long retryInterval = 100;
    }
    
    @Data
    public static class Monitor {
        /**
         * 是否启用监控
         */
        private boolean enabled = true;
        
        /**
         * 指标保留时间（毫秒）
         */
        private long metricsRetentionTime = 3600000; // 1小时
    }
    
    @Data
    public static class HealthCheck {
        /**
         * 是否启用健康检查
         */
        private boolean enabled = true;
        
        /**
         * 健康检查间隔（毫秒）
         */
        private long interval = 30000;
        
        /**
         * 健康检查超时时间（毫秒）
         */
        private long timeout = 5000;
    }
}