package org.vivi.framework.lock.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.vivi.framework.lock.manager.DistributedLockManager;
import org.vivi.framework.lock.manager.LockNamingConvention;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式锁使用示例
 * 展示各种场景下的锁使用方式
 */
@Slf4j
@Service
public class LockUsageExample {

    @Autowired
    private DistributedLockManager lockManager;

    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * 基础锁使用示例
     */
    public void basicLockExample(String businessKey) {
        String lockKey = LockNamingConvention.businessLock("order", "payment", "process");

        // 推荐使用方式：自动管理锁
        lockManager.executeWithLock(lockKey, 30000, () -> {
            log.info("执行业务逻辑: {}", businessKey);
            // 模拟业务处理
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("业务逻辑执行完成: {}", businessKey);
        });
    }

    /**
     * 带返回值的锁使用示例
     */
    public String lockWithReturnExample(String businessKey) {
        String lockKey = LockNamingConvention.businessLock("order", "payment", "result");

        return lockManager.executeWithLock(lockKey, 30000, () -> {
            log.info("执行业务逻辑并返回结果: {}", businessKey);
            // 模拟业务处理
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "处理结果: " + businessKey + "_" + counter.incrementAndGet();
        });
    }

    /**
     * 用户锁使用示例
     */
    public void userLockExample(String userId, String operation) {
        String lockKey = LockNamingConvention.userLock(userId, operation);

        lockManager.executeWithLock(lockKey, 10000, () -> {
            log.info("处理用户操作: userId={}, operation={}", userId, operation);
            // 模拟用户操作处理
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * 资源锁使用示例
     */
    public void resourceLockExample(String resourceType, String resourceId) {
        String lockKey = LockNamingConvention.resourceLock(resourceType, resourceId);

        lockManager.executeWithLock(lockKey, 15000, () -> {
            log.info("处理资源操作: type={}, id={}", resourceType, resourceId);
            // 模拟资源操作处理
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * 定时任务锁使用示例
     */
    @Scheduled(cron = "0 */5 * * * ?") // 每5分钟执行一次
    public void scheduledTaskLockExample() {
        String lockKey = LockNamingConvention.scheduledTaskLock("data-sync");

        lockManager.executeWithLock(lockKey, 60000, () -> {
            log.info("执行定时任务: 数据同步");
            // 模拟定时任务执行
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("定时任务执行完成: 数据同步");
        });
    }

    /**
     * 批量处理锁使用示例
     */
    public void batchProcessLockExample(List<String> dataIds) {
        // 批量处理时使用分段锁
        int segmentSize = 100;
        for (int i = 0; i < dataIds.size(); i += segmentSize) {
            int endIndex = Math.min(i + segmentSize, dataIds.size());
            List<String> segment = dataIds.subList(i, endIndex);

            String lockKey = "batch:" + (i / segmentSize);
            lockManager.executeWithLock(lockKey, 30000, () -> {
                //log.info("处理数据段: 索引={}, 数量={}", i / segmentSize, segment.size());
                // 模拟批量数据处理
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    /**
     * 重试锁使用示例
     */
    public void retryLockExample(String businessKey) {
        String lockKey = LockNamingConvention.businessLock("order", "payment", "retry");

        try {
            lockManager.executeWithRetry(lockKey, 10000, () -> {
                log.info("执行业务逻辑（带重试）: {}", businessKey);
                // 模拟可能失败的业务处理
                if (Math.random() < 0.3) {
                    throw new RuntimeException("模拟业务处理失败");
                }
                return "处理成功: " + businessKey;
            }, 3);
        } catch (Exception e) {
            log.error("业务处理最终失败: {}", businessKey, e);
        }
    }

    /**
     * 幂等性锁使用示例
     */
    public void idempotentLockExample(String business, String uniqueId) {
        String lockKey = LockNamingConvention.idempotentLock(business, uniqueId);

        lockManager.executeWithLock(lockKey, 30000, () -> {
            log.info("执行幂等性操作: business={}, uniqueId={}", business, uniqueId);
            // 模拟幂等性操作
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * 限流锁使用示例
     */
    public boolean rateLimitExample(String resource, int limit, long window) {
        String lockKey = LockNamingConvention.rateLimitLock(resource, String.valueOf(window));

        return lockManager.executeWithLock(lockKey, 5000, () -> {
            // 获取当前计数
            int currentCount = getCurrentCount(resource);
            if (currentCount < limit) {
                // 增加计数
                incrementCount(resource, window);
                return true;
            }
            return false;
        });
    }

    /**
     * 手动锁管理示例
     */
    public void manualLockExample(String businessKey) {
        String lockKey = LockNamingConvention.businessLock("order", "payment", "manual");

        // 手动获取锁
        var lockResult = lockManager.tryLock(lockKey, 30000);
        if (lockResult.isSuccess()) {
            try {
                log.info("手动管理锁 - 执行业务逻辑: {}", businessKey);
                // 模拟业务处理
                Thread.sleep(1000);
                log.info("手动管理锁 - 业务逻辑执行完成: {}", businessKey);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // 手动释放锁
                lockManager.unlock(lockKey, lockResult.getLockValue());
            }
        } else {
            log.warn("获取锁失败: {}", businessKey);
        }
    }

    // 模拟方法
    private int getCurrentCount(String resource) {
        // 模拟获取当前计数
        return (int) (Math.random() * 10);
    }

    private void incrementCount(String resource, long window) {
        // 模拟增加计数
        log.debug("增加计数: resource={}, window={}", resource, window);
    }
}