package org.vivi.framework.lock;

import org.junit.jupiter.api.Test;
import org.vivi.framework.lock.manager.LockNamingConvention;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 锁命名规范测试
 */
class LockNamingConventionTest {
    
    @Test
    void testBusinessLock() {
        String lockKey = LockNamingConvention.businessLock("order", "payment", "process");
        assertEquals("business:order:payment:process", lockKey);
    }
    
    @Test
    void testUserLock() {
        String lockKey = LockNamingConvention.userLock("user123", "profile-update");
        assertEquals("user:user123:profile-update", lockKey);
    }
    
    @Test
    void testResourceLock() {
        String lockKey = LockNamingConvention.resourceLock("file", "file123");
        assertEquals("resource:file:file123", lockKey);
    }
    
    @Test
    void testScheduledTaskLock() {
        String lockKey = LockNamingConvention.scheduledTaskLock("daily-report");
        assertEquals("scheduled:daily-report", lockKey);
    }
    
    @Test
    void testDataLock() {
        String lockKey = LockNamingConvention.dataLock("user", "user123", "update");
        assertEquals("data:user:user123:update", lockKey);
    }
    
    @Test
    void testCacheLock() {
        String lockKey = LockNamingConvention.cacheLock("user:profile:123");
        assertEquals("cache:user:profile:123", lockKey);
    }
    
    @Test
    void testRateLimitLock() {
        String lockKey = LockNamingConvention.rateLimitLock("api", "60000");
        assertEquals("rate-limit:api:60000", lockKey);
    }
    
    @Test
    void testIdempotentLock() {
        String lockKey = LockNamingConvention.idempotentLock("order", "order123");
        assertEquals("idempotent:order:order123", lockKey);
    }
    
    @Test
    void testDistributedLock() {
        String lockKey = LockNamingConvention.distributedLock("order", "payment", "process");
        assertEquals("distributed:order:payment:process", lockKey);
    }
    
    @Test
    void testClusterLock() {
        String lockKey = LockNamingConvention.clusterLock("main-cluster", "shutdown");
        assertEquals("cluster:main-cluster:shutdown", lockKey);
    }
    
    @Test
    void testNodeLock() {
        String lockKey = LockNamingConvention.nodeLock("node1", "restart");
        assertEquals("node:node1:restart", lockKey);
    }
    
    @Test
    void testSessionLock() {
        String lockKey = LockNamingConvention.sessionLock("session123", "logout");
        assertEquals("session:session123:logout", lockKey);
    }
    
    @Test
    void testFileLock() {
        String lockKey = LockNamingConvention.fileLock("/path/to/file.txt", "read");
        assertEquals("file:/path/to/file.txt:read", lockKey);
    }
    
    @Test
    void testDatabaseLock() {
        String lockKey = LockNamingConvention.databaseLock("mydb", "users", "insert");
        assertEquals("database:mydb:users:insert", lockKey);
    }
    
    @Test
    void testApiLock() {
        String lockKey = LockNamingConvention.apiLock("/api/users", "POST");
        assertEquals("api:/api/users:POST", lockKey);
    }
    
    @Test
    void testTempLock() {
        String lockKey = LockNamingConvention.tempLock("test");
        assertTrue(lockKey.startsWith("temp:test:"));
        assertTrue(lockKey.length() > "temp:test:".length());
    }
    
    @Test
    void testGlobalLock() {
        String lockKey = LockNamingConvention.globalLock("maintenance");
        assertEquals("global:maintenance", lockKey);
    }
    
    @Test
    void testCustomLock() {
        String lockKey = LockNamingConvention.customLock("custom", "part1", "part2", "part3");
        assertEquals("custom:part1:part2:part3", lockKey);
    }
    
    @Test
    void testIsValidLockKey() {
        assertTrue(LockNamingConvention.isValidLockKey("valid:lock:key"));
        assertTrue(LockNamingConvention.isValidLockKey("simple"));
        assertFalse(LockNamingConvention.isValidLockKey(""));
        assertFalse(LockNamingConvention.isValidLockKey(null));
        assertFalse(LockNamingConvention.isValidLockKey("key with spaces"));
        assertFalse(LockNamingConvention.isValidLockKey("key\twith\ttabs"));
        assertFalse(LockNamingConvention.isValidLockKey("key\nwith\nnewlines"));
    }
    
    @Test
    void testSanitizeLockKey() {
        assertEquals("valid_lock_key", LockNamingConvention.sanitizeLockKey("valid:lock:key"));
        assertEquals("key_with_spaces", LockNamingConvention.sanitizeLockKey("key with spaces"));
        assertEquals("key_with_special_chars", LockNamingConvention.sanitizeLockKey("key@#$%^&*()with!@#$%^&*()special!@#$%^&*()chars"));
        assertEquals("", LockNamingConvention.sanitizeLockKey(null));
    }
    
    @Test
    void testGetNamespace() {
        assertEquals("business", LockNamingConvention.getNamespace("business:order:payment"));
        assertEquals("user", LockNamingConvention.getNamespace("user:123:update"));
        assertEquals("", LockNamingConvention.getNamespace("simple"));
        assertEquals("", LockNamingConvention.getNamespace(""));
        assertEquals("", LockNamingConvention.getNamespace(null));
    }
    
    @Test
    void testGetBusiness() {
        assertEquals("order", LockNamingConvention.getBusiness("business:order:payment"));
        assertEquals("user", LockNamingConvention.getBusiness("user:123:update"));
        assertEquals("", LockNamingConvention.getBusiness("simple"));
        assertEquals("", LockNamingConvention.getBusiness(""));
        assertEquals("", LockNamingConvention.getBusiness(null));
    }
}