package org.vivi.framework.lock.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vivi.framework.lock.metrics.LockMetrics;
import org.vivi.framework.lock.metrics.LockStatistics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * 分布式锁监控器
 * 监控锁的使用情况、性能指标和异常信息
 */
@Slf4j
@Component
public class LockMonitor {

    /**
     * 锁指标缓存
     */
    private final Map<String, LockMetrics> metricsMap = new ConcurrentHashMap<>();

    /**
     * 记录锁获取尝试
     */
    public void recordLockAttempt(String lockKey, String strategy, boolean success, long acquireTime) {
        LockMetrics metrics = metricsMap.computeIfAbsent(lockKey,
                k -> LockMetrics.builder()
                        .lockKey(lockKey)
                        .strategy(strategy)
                        .build());

        synchronized (metrics) {
            metrics.setTotalAttempts(metrics.getTotalAttempts() + 1);
            if (success) {
                metrics.setSuccessAttempts(metrics.getSuccessAttempts() + 1);
                metrics.setLastAcquireTime(System.currentTimeMillis());
                metrics.setCurrentHolder(Thread.currentThread().getName());
            } else {
                metrics.setFailedAttempts(metrics.getFailedAttempts() + 1);
            }
            metrics.setTotalAcquireTime(metrics.getTotalAcquireTime() + acquireTime);
            if (metrics.getTotalAttempts() > 0) {
                metrics.setAverageAcquireTime(metrics.getTotalAcquireTime() / metrics.getTotalAttempts());
            }
        }

        log.debug("锁获取记录: lockKey={}, strategy={}, success={}, acquireTime={}ms",
                lockKey, strategy, success, acquireTime);
    }

    /**
     * 记录锁释放
     */
    public void recordUnlock(String lockKey, String strategy, boolean success) {
        LockMetrics metrics = metricsMap.get(lockKey);
        if (metrics != null && success) {
            synchronized (metrics) {
                metrics.setLastReleaseTime(System.currentTimeMillis());
                metrics.setCurrentHolder(null);
            }
        }

        log.debug("锁释放记录: lockKey={}, strategy={}, success={}",
                lockKey, strategy, success);
    }

    /**
     * 记录锁获取错误
     */
    public void recordLockError(String lockKey, String strategy, Exception error) {
        LockMetrics metrics = metricsMap.computeIfAbsent(lockKey,
                k -> LockMetrics.builder()
                        .lockKey(lockKey)
                        .strategy(strategy)
                        .build());

        synchronized (metrics) {
            metrics.setErrorCount(metrics.getErrorCount() + 1);
            metrics.setLastError(error.getMessage());
            metrics.setLastErrorTime(System.currentTimeMillis());
        }

        log.error("锁获取错误: lockKey={}, strategy={}, error={}",
                lockKey, strategy, error.getMessage(), error);
    }

    /**
     * 记录锁释放错误
     */
    public void recordUnlockError(String lockKey, String strategy, Exception error) {
        LockMetrics metrics = metricsMap.get(lockKey);
        if (metrics != null) {
            synchronized (metrics) {
                metrics.setErrorCount(metrics.getErrorCount() + 1);
                metrics.setLastError(error.getMessage());
                metrics.setLastErrorTime(System.currentTimeMillis());
            }
        }

        log.error("锁释放错误: lockKey={}, strategy={}, error={}",
                lockKey, strategy, error.getMessage(), error);
    }

    /**
     * 获取指定锁的指标
     */
    public LockMetrics getMetrics(String lockKey) {
        return metricsMap.get(lockKey);
    }

    /**
     * 获取所有锁指标
     */
    public List<LockMetrics> getAllMetrics() {
        return metricsMap.values().stream()
                .sorted((m1, m2) -> Long.compare(m2.getLastAcquireTime(), m1.getLastAcquireTime()))
                .collect(Collectors.toList());
    }

    /**
     * 获取指定策略的锁指标
     */
    public List<LockMetrics> getMetricsByStrategy(String strategy) {
        return metricsMap.values().stream()
                .filter(metrics -> strategy.equals(metrics.getStrategy()))
                .sorted((m1, m2) -> Long.compare(m2.getLastAcquireTime(), m1.getLastAcquireTime()))
                .collect(Collectors.toList());
    }

    /**
     * 获取锁统计信息
     */
    public LockStatistics getStatistics() {
        List<LockMetrics> allMetrics = getAllMetrics();

        long totalAttempts = allMetrics.stream().mapToLong(LockMetrics::getTotalAttempts).sum();
        long successAttempts = allMetrics.stream().mapToLong(LockMetrics::getSuccessAttempts).sum();
        long failedAttempts = allMetrics.stream().mapToLong(LockMetrics::getFailedAttempts).sum();
        long errorCount = allMetrics.stream().mapToLong(LockMetrics::getErrorCount).sum();

        double successRate = totalAttempts > 0 ? (double) successAttempts / totalAttempts : 0.0;
        double averageAcquireTime = allMetrics.stream()
                .mapToLong(LockMetrics::getAverageAcquireTime)
                .average()
                .orElse(0.0);

        return LockStatistics.builder()
                .totalLocks(allMetrics.size())
                .totalAttempts(totalAttempts)
                .successAttempts(successAttempts)
                .failedAttempts(failedAttempts)
                .errorCount(errorCount)
                .successRate(successRate)
                .averageAcquireTime(averageAcquireTime)
                .build();
    }

    /**
     * 清理过期的指标数据
     */
    public void cleanupExpiredMetrics(long expireTime) {
        long currentTime = System.currentTimeMillis();
        metricsMap.entrySet().removeIf(entry -> {
            LockMetrics metrics = entry.getValue();
            return currentTime - metrics.getLastAcquireTime() > expireTime;
        });
    }

    /**
     * 重置所有指标
     */
    public void resetMetrics() {
        metricsMap.clear();
        log.info("锁监控指标已重置");
    }
}