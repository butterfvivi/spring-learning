package org.vivi.framework.lock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.lock.manager.DistributedLockManager;
import org.vivi.framework.lock.metrics.LockMetrics;
import org.vivi.framework.lock.metrics.LockStatistics;
import org.vivi.framework.lock.monitor.LockHealthChecker;
import org.vivi.framework.lock.monitor.LockMonitor;

import java.util.List;
import java.util.Map;

/**
 * 分布式锁监控控制器
 * 提供锁监控相关的REST API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/lock")
public class LockMonitorController {
    
    @Autowired
    private DistributedLockManager lockManager;
    
    @Autowired
    private LockMonitor lockMonitor;
    
    @Autowired
    private LockHealthChecker lockHealthChecker;
    
    /**
     * 获取锁统计信息
     */
    @GetMapping("/statistics")
    public LockStatistics getLockStatistics() {
        log.debug("获取锁统计信息");
        return lockMonitor.getStatistics();
    }
    
    /**
     * 获取所有锁指标
     */
    @GetMapping("/metrics")
    public List<LockMetrics> getAllMetrics() {
        log.debug("获取所有锁指标");
        return lockMonitor.getAllMetrics();
    }
    
    /**
     * 获取指定锁的指标
     */
    @GetMapping("/metrics/{lockKey}")
    public LockMetrics getLockMetrics(@PathVariable String lockKey) {
        log.debug("获取锁指标: lockKey={}", lockKey);
        return lockMonitor.getMetrics(lockKey);
    }
    
    /**
     * 获取指定策略的锁指标
     */
    @GetMapping("/metrics/strategy/{strategy}")
    public List<LockMetrics> getMetricsByStrategy(@PathVariable String strategy) {
        log.debug("获取策略锁指标: strategy={}", strategy);
        return lockMonitor.getMetricsByStrategy(strategy);
    }
    
    /**
     * 获取健康状态
     */
    @GetMapping("/health")
    public Map<String, Boolean> getHealthStatus() {
        log.debug("获取锁健康状态");
        return lockHealthChecker.getAllStrategiesHealth();
    }
    
    /**
     * 检查指定策略的健康状态
     */
    @GetMapping("/health/{strategy}")
    public boolean getStrategyHealth(@PathVariable String strategy) {
        log.debug("检查策略健康状态: strategy={}", strategy);
        return lockHealthChecker.isStrategyHealthy(strategy);
    }
    
    /**
     * 手动触发健康检查
     */
    @PostMapping("/health/check")
    public void triggerHealthCheck() {
        log.info("手动触发健康检查");
        lockHealthChecker.triggerHealthCheck();
    }
    
    /**
     * 重置监控指标
     */
    @PostMapping("/metrics/reset")
    public void resetMetrics() {
        log.info("重置监控指标");
        lockMonitor.resetMetrics();
    }
    
    /**
     * 清理过期指标
     */
    @PostMapping("/metrics/cleanup")
    public void cleanupMetrics(@RequestParam(defaultValue = "3600000") long expireTime) {
        log.info("清理过期指标: expireTime={}ms", expireTime);
        lockMonitor.cleanupExpiredMetrics(expireTime);
    }
    
    /**
     * 检查锁是否存在
     */
    @GetMapping("/check/{lockKey}")
    public boolean isLocked(@PathVariable String lockKey) {
        log.debug("检查锁状态: lockKey={}", lockKey);
        return lockManager.isLocked(lockKey);
    }
    
    /**
     * 检查锁是否存在（指定策略）
     */
    @GetMapping("/check/{lockKey}/{strategy}")
    public boolean isLocked(@PathVariable String lockKey, @PathVariable String strategy) {
        log.debug("检查锁状态: lockKey={}, strategy={}", lockKey, strategy);
        return lockManager.isLocked(lockKey);
    }
    
    /**
     * 获取统计报告
     */
    @GetMapping("/report")
    public String getReport() {
        log.debug("获取统计报告");
        LockStatistics statistics = lockMonitor.getStatistics();
        return statistics.generateReport();
    }
}