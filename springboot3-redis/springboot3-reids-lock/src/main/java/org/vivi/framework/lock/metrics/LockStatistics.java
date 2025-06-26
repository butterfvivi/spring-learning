package org.vivi.framework.lock.metrics;

import lombok.Builder;
import lombok.Data;

/**
 * 锁统计信息
 * 汇总所有锁的使用情况和性能指标
 */
@Data
@Builder
public class LockStatistics {
    
    /**
     * 总锁数量
     */
    private long totalLocks;
    
    /**
     * 总尝试次数
     */
    private long totalAttempts;
    
    /**
     * 成功获取次数
     */
    private long successAttempts;
    
    /**
     * 失败获取次数
     */
    private long failedAttempts;
    
    /**
     * 错误次数
     */
    private long errorCount;
    
    /**
     * 成功率
     */
    private double successRate;
    
    /**
     * 失败率
     */
    private double failureRate;
    
    /**
     * 错误率
     */
    private double errorRate;
    
    /**
     * 平均获取时间
     */
    private double averageAcquireTime;
    
    /**
     * 当前被持有的锁数量
     */
    private long heldLocks;
    
    /**
     * 计算失败率
     */
    public double getFailureRate() {
        return totalAttempts > 0 ? (double) failedAttempts / totalAttempts : 0.0;
    }
    
    /**
     * 计算错误率
     */
    public double getErrorRate() {
        return totalAttempts > 0 ? (double) errorCount / totalAttempts : 0.0;
    }
    
    /**
     * 获取持有率
     */
    public double getHoldRate() {
        return totalLocks > 0 ? (double) heldLocks / totalLocks : 0.0;
    }
    
    /**
     * 检查系统健康状态
     */
    public LockHealthStatus getHealthStatus() {
        if (errorRate > 0.1) { // 错误率超过10%
            return LockHealthStatus.UNHEALTHY;
        } else if (successRate < 0.8) { // 成功率低于80%
            return LockHealthStatus.WARNING;
        } else {
            return LockHealthStatus.HEALTHY;
        }
    }
    
    /**
     * 获取性能等级
     */
    public LockPerformanceLevel getPerformanceLevel() {
        if (averageAcquireTime < 10) {
            return LockPerformanceLevel.EXCELLENT;
        } else if (averageAcquireTime < 50) {
            return LockPerformanceLevel.GOOD;
        } else if (averageAcquireTime < 100) {
            return LockPerformanceLevel.FAIR;
        } else {
            return LockPerformanceLevel.POOR;
        }
    }
    
    /**
     * 生成统计报告
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== 分布式锁统计报告 ===\n");
        report.append(String.format("总锁数量: %d\n", totalLocks));
        report.append(String.format("总尝试次数: %d\n", totalAttempts));
        report.append(String.format("成功次数: %d (%.2f%%)\n", successAttempts, successRate * 100));
        report.append(String.format("失败次数: %d (%.2f%%)\n", failedAttempts, getFailureRate() * 100));
        report.append(String.format("错误次数: %d (%.2f%%)\n", errorCount, getErrorRate() * 100));
        report.append(String.format("平均获取时间: %.2fms\n", averageAcquireTime));
        report.append(String.format("当前持有锁: %d (%.2f%%)\n", heldLocks, getHoldRate() * 100));
        report.append(String.format("系统状态: %s\n", getHealthStatus()));
        report.append(String.format("性能等级: %s\n", getPerformanceLevel()));
        return report.toString();
    }
    
    /**
     * 锁健康状态枚举
     */
    public enum LockHealthStatus {
        HEALTHY("健康"),
        WARNING("警告"),
        UNHEALTHY("不健康");
        
        private final String description;
        
        LockHealthStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 锁性能等级枚举
     */
    public enum LockPerformanceLevel {
        EXCELLENT("优秀"),
        GOOD("良好"),
        FAIR("一般"),
        POOR("较差");
        
        private final String description;
        
        LockPerformanceLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}