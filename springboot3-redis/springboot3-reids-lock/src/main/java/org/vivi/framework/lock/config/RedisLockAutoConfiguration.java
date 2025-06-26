package org.vivi.framework.lock.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.vivi.framework.lock.manager.DistributedLockManager;
import org.vivi.framework.lock.monitor.LockHealthChecker;
import org.vivi.framework.lock.monitor.LockMonitor;
import org.vivi.framework.lock.service.LockStrategy;
import org.vivi.framework.lock.service.impl.RedisLockStrategy;

/**
 * Redis分布式锁自动配置类
 */
@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnClass(StringRedisTemplate.class)
@EnableConfigurationProperties(RedisLockProperties.class)
@ConditionalOnProperty(prefix = "vivi.redis-lock", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisLockAutoConfiguration {

    /**
     * Redis分布式锁策略
     */
    @Bean
    @ConditionalOnMissingBean(name = "redis")
    public LockStrategy redisLockStrategy(StringRedisTemplate redisTemplate) {
        log.info("初始化Redis分布式锁策略");
        return new RedisLockStrategy();
    }

    /**
     * 锁监控器
     */
    @Bean
    @ConditionalOnMissingBean
    public LockMonitor lockMonitor() {
        log.info("初始化锁监控器");
        return new LockMonitor();
    }

    /**
     * 锁健康检查器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "vivi.redis-lock.health-check", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LockHealthChecker lockHealthChecker() {
        log.info("初始化锁健康检查器");
        return new LockHealthChecker();
    }

    /**
     * 分布式锁管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public DistributedLockManager distributedLockManager() {
        log.info("初始化分布式锁管理器");
        return new DistributedLockManager();
    }
}