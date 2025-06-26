package org.vivi.framework.lock.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.vivi.framework.lock.manager.DistributedLockManager;
import org.vivi.framework.lock.metrics.LockStatistics;
import org.vivi.framework.lock.model.LockResult;
import org.vivi.framework.lock.service.LockStrategy;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁健康检查器
 * 定期检查分布式锁服务的健康状态，确保锁服务的可用性
 */
@Slf4j
@Component
public class LockHealthChecker {
    
    @Autowired
    private Map<String, LockStrategy> lockStrategies;
    
    @Autowired
    private LockMonitor lockMonitor;
    
    @Autowired
    private DistributedLockManager lockManager;
    
    /**
     * 健康检查间隔（毫秒）
     */
    private static final long HEALTH_CHECK_INTERVAL = 30000; // 30秒
    
    /**
     * 锁超时时间（毫秒）
     */
    private static final long LOCK_TIMEOUT = 5000; // 5秒
    
    /**
     * 定期健康检查
     */
    @Scheduled(fixedRate = HEALTH_CHECK_INTERVAL)
    public void healthCheck() {
        log.debug("开始分布式锁健康检查...");
        
        for (Map.Entry<String, LockStrategy> entry : lockStrategies.entrySet()) {
            String strategyName = entry.getKey();
            LockStrategy strategy = entry.getValue();
            
            try {
                performHealthCheck(strategyName, strategy);
            } catch (Exception e) {
                log.error("锁策略健康检查异常: {}", strategyName, e);
                recordHealthCheckFailure(strategyName, e);
            }
        }
        
        // 检查整体锁服务状态
        checkOverallHealth();
        
        log.debug("分布式锁健康检查完成");
    }
    
    /**
     * 执行单个锁策略的健康检查
     */
    private void performHealthCheck(String strategyName, LockStrategy strategy) {
        String testLockKey = "health-check-" + strategyName;
        String testLockValue = "health-check-" + System.currentTimeMillis();
        
        try {
            // 尝试获取测试锁
            LockResult lockResult = strategy.tryLock(testLockKey, testLockValue, LOCK_TIMEOUT);
            
            if (lockResult.isSuccess()) {
                // 成功获取锁，立即释放
                boolean unlockSuccess = strategy.unlock(testLockKey, testLockValue);
                
                if (unlockSuccess) {
                    log.debug("锁策略健康检查通过: {}", strategyName);
                    recordHealthCheckSuccess(strategyName);
                } else {
                    log.warn("锁策略健康检查警告: {} - 锁释放失败", strategyName);
                    recordHealthCheckWarning(strategyName, "锁释放失败");
                }
            } else {
                log.warn("锁策略健康检查失败: {} - 无法获取锁", strategyName);
                recordHealthCheckFailure(strategyName, new RuntimeException("无法获取锁"));
            }
            
        } catch (Exception e) {
            log.error("锁策略健康检查异常: {}", strategyName, e);
            recordHealthCheckFailure(strategyName, e);
        }
    }
    
    /**
     * 检查整体锁服务状态
     */
    private void checkOverallHealth() {
        try {
            LockStatistics statistics = lockMonitor.getStatistics();
            
            // 检查成功率
            if (statistics.getSuccessRate() < 0.8) {
                log.warn("锁服务整体成功率较低: {:.2f}%", statistics.getSuccessRate() * 100);
            }
            
            // 检查错误率
            if (statistics.getErrorRate() > 0.1) {
                log.error("锁服务整体错误率较高: {:.2f}%", statistics.getErrorRate() * 100);
            }
            
            // 检查平均获取时间
            if (statistics.getAverageAcquireTime() > 100) {
                log.warn("锁服务平均获取时间较长: {:.2f}ms", statistics.getAverageAcquireTime());
            }
            
            // 记录健康状态
            LockStatistics.LockHealthStatus healthStatus = statistics.getHealthStatus();
            switch (healthStatus) {
                case HEALTHY:
                    log.debug("锁服务整体状态: 健康");
                    break;
                case WARNING:
                    log.warn("锁服务整体状态: 警告");
                    break;
                case UNHEALTHY:
                    log.error("锁服务整体状态: 不健康");
                    break;
            }
            
        } catch (Exception e) {
            log.error("检查整体锁服务状态异常", e);
        }
    }
    
    /**
     * 记录健康检查成功
     */
    private void recordHealthCheckSuccess(String strategyName) {
        // 可以在这里添加健康检查成功的记录逻辑
        // 例如：更新健康状态缓存、发送成功通知等
    }
    
    /**
     * 记录健康检查警告
     */
    private void recordHealthCheckWarning(String strategyName, String warning) {
        // 可以在这里添加健康检查警告的记录逻辑
        // 例如：记录警告日志、发送警告通知等
        log.warn("锁策略健康检查警告: {} - {}", strategyName, warning);
    }
    
    /**
     * 记录健康检查失败
     */
    private void recordHealthCheckFailure(String strategyName, Exception error) {
        // 可以在这里添加健康检查失败的记录逻辑
        // 例如：记录失败日志、发送告警通知等
        log.error("锁策略健康检查失败: {} - {}", strategyName, error.getMessage());
    }
    
    /**
     * 手动触发健康检查
     */
    public void triggerHealthCheck() {
        log.info("手动触发分布式锁健康检查");
        healthCheck();
    }
    
    /**
     * 检查指定锁策略的健康状态
     */
    public boolean isStrategyHealthy(String strategyName) {
        LockStrategy strategy = lockStrategies.get(strategyName);
        if (strategy == null) {
            log.warn("锁策略不存在: {}", strategyName);
            return false;
        }
        
        String testLockKey = "health-check-" + strategyName;
        String testLockValue = "health-check-" + System.currentTimeMillis();
        
        try {
            LockResult lockResult = strategy.tryLock(testLockKey, testLockValue, LOCK_TIMEOUT);
            if (lockResult.isSuccess()) {
                boolean unlockSuccess = strategy.unlock(testLockKey, testLockValue);
                return unlockSuccess;
            }
            return false;
        } catch (Exception e) {
            log.error("检查锁策略健康状态异常: {}", strategyName, e);
            return false;
        }
    }
    
    /**
     * 获取所有锁策略的健康状态
     */
    public Map<String, Boolean> getAllStrategiesHealth() {
        Map<String, Boolean> healthStatus = new java.util.HashMap<>();
        
        for (String strategyName : lockStrategies.keySet()) {
            healthStatus.put(strategyName, isStrategyHealthy(strategyName));
        }
        
        return healthStatus;
    }
    
    /**
     * 清理过期的健康检查数据
     */
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void cleanupHealthCheckData() {
        try {
            // 清理过期的监控指标（保留1小时的数据）
            lockMonitor.cleanupExpiredMetrics(TimeUnit.HOURS.toMillis(1));
            log.debug("清理过期的健康检查数据完成");
        } catch (Exception e) {
            log.error("清理过期的健康检查数据异常", e);
        }
    }
}