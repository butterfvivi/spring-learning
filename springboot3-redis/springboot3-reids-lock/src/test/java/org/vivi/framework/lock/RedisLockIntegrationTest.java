package org.vivi.framework.lock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.vivi.framework.lock.example.LockUsageExample;
import org.vivi.framework.lock.manager.DistributedLockManager;
import org.vivi.framework.lock.manager.LockNamingConvention;
import org.vivi.framework.lock.model.LockResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis分布式锁集成测试
 */
@SpringBootTest
@ActiveProfiles("test")
class RedisLockIntegrationTest {
    
    @Autowired
    private DistributedLockManager lockManager;
    
    @Autowired
    private LockUsageExample lockUsageExample;
    
    @Test
    void testBasicLockIntegration() {
        String lockKey = "integration:test:lock";
        
        // 测试锁获取
        LockResult result = lockManager.tryLock(lockKey, 10000);
        assertTrue(result.isSuccess());
        
        // 测试重复获取失败
        LockResult result2 = lockManager.tryLock(lockKey, 10000);
        assertFalse(result2.isSuccess());
        
        // 测试锁释放
        boolean unlockResult = lockManager.unlock(lockKey, result.getLockValue());
        assertTrue(unlockResult);
        
        // 测试锁释放后可以重新获取
        LockResult result3 = lockManager.tryLock(lockKey, 10000);
        assertTrue(result3.isSuccess());
        
        // 清理
        lockManager.unlock(lockKey, result3.getLockValue());
    }
    
    @Test
    void testConcurrentLockIntegration() throws InterruptedException {
        String lockKey = "integration:concurrent:lock";
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    lockManager.executeWithLock(lockKey, 1000, () -> {
                        successCount.incrementAndGet();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return null;
                    });
                } catch (Exception e) {
                    // 锁获取失败
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        // 验证只有一个线程成功获取锁
        assertEquals(1, successCount.get());
    }
    
    @Test
    void testLockUsageExamples() {
        // 测试基础锁使用
        assertDoesNotThrow(() -> {
            lockUsageExample.basicLockExample("test-business-key");
        });
        
        // 测试带返回值的锁使用
        String result = lockUsageExample.lockWithReturnExample("test-business-key");
        assertNotNull(result);
        assertTrue(result.startsWith("处理结果:"));
        
        // 测试用户锁使用
        assertDoesNotThrow(() -> {
            lockUsageExample.userLockExample("user123", "profile-update");
        });
        
        // 测试资源锁使用
        assertDoesNotThrow(() -> {
            lockUsageExample.resourceLockExample("file", "file123");
        });
        
        // 测试批量处理锁使用
        List<String> dataIds = Arrays.asList("1", "2", "3", "4", "5");
        assertDoesNotThrow(() -> {
            lockUsageExample.batchProcessLockExample(dataIds);
        });
        
        // 测试手动锁管理
        assertDoesNotThrow(() -> {
            lockUsageExample.manualLockExample("test-business-key");
        });
    }
    
    @Test
    void testLockNamingConvention() {
        // 测试业务锁命名
        String businessLock = LockNamingConvention.businessLock("order", "payment", "process");
        assertEquals("business:order:payment:process", businessLock);
        
        // 测试用户锁命名
        String userLock = LockNamingConvention.userLock("user123", "update");
        assertEquals("user:user123:update", userLock);
        
        // 测试资源锁命名
        String resourceLock = LockNamingConvention.resourceLock("file", "file123");
        assertEquals("resource:file:file123", resourceLock);
        
        // 测试定时任务锁命名
        String scheduledLock = LockNamingConvention.scheduledTaskLock("daily-report");
        assertEquals("scheduled:daily-report", scheduledLock);
    }
    
    @Test
    void testLockWithRetry() {
        String lockKey = "integration:retry:lock";
        
        // 测试带重试的锁执行
        assertDoesNotThrow(() -> {
            lockUsageExample.retryLockExample("test-retry-key");
        });
    }
    
    @Test
    void testIdempotentLock() {
        // 测试幂等性锁
        assertDoesNotThrow(() -> {
            lockUsageExample.idempotentLockExample("order", "order123");
        });
    }
    
    @Test
    void testRateLimitLock() {
        // 测试限流锁
        boolean result = lockUsageExample.rateLimitExample("api", 10, 60000);
        assertTrue(result); // 第一次应该成功
    }
}