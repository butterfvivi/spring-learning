package org.vivi.framework.lock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.vivi.framework.lock.manager.DistributedLockManager;
import org.vivi.framework.lock.manager.LockAcquisitionException;
import org.vivi.framework.lock.model.LockResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 分布式锁管理器测试
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.redis.host=localhost",
    "spring.data.redis.port=6379",
    "vivi.redis-lock.enabled=true"
})
class DistributedLockManagerTest {
    
    @Autowired
    private DistributedLockManager lockManager;
    
    @Test
    void testBasicLock() {
        String lockKey = "test:basic:lock";
        
        // 测试锁获取和释放
        String result = lockManager.executeWithLock(lockKey, 5000, () -> {
            // 验证锁已被获取
            assertTrue(lockManager.isLocked(lockKey));
            return "success";
        });
        
        assertEquals("success", result);
        
        // 验证锁已被释放
        assertFalse(lockManager.isLocked(lockKey));
    }
    
    @Test
    void testConcurrentLock() throws InterruptedException {
        String lockKey = "test:concurrent:lock";
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
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
            }).start();
        }
        
        latch.await();
        // 验证只有一个线程成功获取锁
        assertEquals(1, successCount.get());
    }
    
    @Test
    void testLockWithRetry() {
        String lockKey = "test:retry:lock";
        
        // 测试带重试的锁执行
        String result = lockManager.executeWithRetry(lockKey, 1000, () -> {
            return "retry-success";
        }, 3);
        
        assertEquals("retry-success", result);
    }
    
    @Test
    void testManualLock() {
        String lockKey = "test:manual:lock";
        
        // 手动管理锁
        LockResult lockResult = lockManager.tryLock(lockKey, 5000);
        assertTrue(lockResult.isSuccess());
        
        // 验证锁已被获取
        assertTrue(lockManager.isLocked(lockKey));
        
        // 尝试重复获取失败
        LockResult lockResult2 = lockManager.tryLock(lockKey, 5000);
        assertFalse(lockResult2.isSuccess());
        
        // 释放锁
        boolean unlockResult = lockManager.unlock(lockKey, lockResult.getLockValue());
        assertTrue(unlockResult);
        
        // 验证锁已被释放
        assertFalse(lockManager.isLocked(lockKey));
    }
    
    @Test
    void testLockTimeout() {
        String lockKey = "test:timeout:lock";
        
        // 测试锁超时
        assertThrows(LockAcquisitionException.class, () -> {
            lockManager.executeWithLock(lockKey, 100, () -> {
                // 模拟长时间执行
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return null;
            });
        });
    }
}