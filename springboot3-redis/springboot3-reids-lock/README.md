 # 分布式锁设计方案

## 1. 概述

分布式锁是分布式系统中的核心组件，用于解决分布式环境下的资源竞争问题。本方案提供多种分布式锁实现，包括Redis分布式锁、Zookeeper分布式锁、数据库分布式锁等，满足不同场景的需求。

## 2. 设计目标

### 2.1 核心特性
- **互斥性**：同一时间只能有一个客户端持有锁
- **防死锁**：锁必须能够自动释放，避免死锁
- **可重入性**：支持同一线程多次获取同一把锁
- **高性能**：锁的获取和释放操作要快速
- **高可用**：锁服务要具备高可用性
- **公平性**：支持公平锁和非公平锁

### 2.2 应用场景
- 分布式任务调度
- 分布式资源管理
- 分布式计数器
- 分布式限流
- 分布式幂等性控制

## 3. 架构设计

### 3.1 整体架构
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   应用层        │    │   应用层        │    │   应用层        │
│                 │    │                 │    │                 │
│ - 业务逻辑      │    │ - 业务逻辑      │    │ - 业务逻辑      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   分布式锁服务   │
                    │                 │
                    │ - 锁管理器      │
                    │ - 锁策略        │
                    │ - 锁监控        │
                    └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Redis集群     │    │  Zookeeper集群  │    │   数据库集群     │
│                 │    │                 │    │                 │
│ - 主从复制      │    │ - 一致性协议    │    │ - 主从复制      │
│ - 哨兵模式      │    │ - 选举机制      │    │ - 读写分离      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 4. 实现方案

### 4.1 Redis分布式锁

#### 4.1.1 基础实现
```java
@Component
public class RedisLockStrategy implements LockStrategy {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String LOCK_PREFIX = "distributed-lock:";
    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "return redis.call('del', KEYS[1]) " +
        "else return 0 end";
    
    @Override
    public LockResult tryLock(String lockKey, String lockValue, long expireTime) {
        String key = LOCK_PREFIX + lockKey;
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(key, lockValue, expireTime, TimeUnit.MILLISECONDS);
        
        return LockResult.builder()
            .success(Boolean.TRUE.equals(success))
            .lockValue(lockValue)
            .build();
    }
    
    @Override
    public boolean unlock(String lockKey, String lockValue) {
        String key = LOCK_PREFIX + lockKey;
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(UNLOCK_SCRIPT);
        script.setResultType(Long.class);
        
        Long result = redisTemplate.execute(script, 
            Collections.singletonList(key), lockValue);
        return Long.valueOf(1).equals(result);
    }
}
```

#### 4.1.2 可重入锁实现
```java
@Component
public class RedisReentrantLockStrategy implements LockStrategy {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String LOCK_PREFIX = "reentrant-lock:";
    private static final String LOCK_SCRIPT = 
        "local key = KEYS[1] " +
        "local value = ARGV[1] " +
        "local expire = ARGV[2] " +
        "local count = redis.call('hget', key, value) " +
        "if count then " +
        "  redis.call('hincrby', key, value, 1) " +
        "  redis.call('expire', key, expire) " +
        "  return 1 " +
        "else " +
        "  local exists = redis.call('hlen', key) " +
        "  if exists == 0 then " +
        "    redis.call('hset', key, value, 1) " +
        "    redis.call('expire', key, expire) " +
        "    return 1 " +
        "  else " +
        "    return 0 " +
        "  end " +
        "end";
    
    @Override
    public LockResult tryLock(String lockKey, String lockValue, long expireTime) {
        String key = LOCK_PREFIX + lockKey;
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(LOCK_SCRIPT);
        script.setResultType(Long.class);
        
        Long result = redisTemplate.execute(script, 
            Collections.singletonList(key), lockValue, String.valueOf(expireTime));
        
        return LockResult.builder()
            .success(Long.valueOf(1).equals(result))
            .lockValue(lockValue)
            .build();
    }
}
```

### 4.2 Zookeeper分布式锁

```java
@Component
public class ZookeeperLockStrategy implements LockStrategy {
    
    @Autowired
    private CuratorFramework curatorClient;
    
    private static final String LOCK_PATH = "/distributed-locks/";
    private final Map<String, InterProcessMutex> mutexMap = new ConcurrentHashMap<>();
    
    @Override
    public LockResult tryLock(String lockKey, String lockValue, long expireTime) {
        String lockPath = LOCK_PATH + lockKey;
        InterProcessMutex mutex = mutexMap.computeIfAbsent(lockKey, 
            k -> new InterProcessMutex(curatorClient, lockPath));
        
        try {
            boolean acquired = mutex.acquire(expireTime, TimeUnit.MILLISECONDS);
            return LockResult.builder()
                .success(acquired)
                .lockValue(lockValue)
                .build();
        } catch (Exception e) {
            log.error("Zookeeper锁获取失败: lockKey={}", lockKey, e);
            return LockResult.builder().success(false).build();
        }
    }
    
    @Override
    public boolean unlock(String lockKey, String lockValue) {
        InterProcessMutex mutex = mutexMap.get(lockKey);
        if (mutex != null && mutex.isAcquiredInThisProcess()) {
            try {
                mutex.release();
                mutexMap.remove(lockKey);
                return true;
            } catch (Exception e) {
                log.error("Zookeeper锁释放失败: lockKey={}", lockKey, e);
            }
        }
        return false;
    }
}
```

### 4.3 数据库分布式锁

```java
@Component
public class DatabaseLockStrategy implements LockStrategy {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final String LOCK_TABLE = "distributed_lock";
    
    @Override
    public LockResult tryLock(String lockKey, String lockValue, long expireTime) {
        String sql = "INSERT INTO " + LOCK_TABLE + 
                    " (lock_key, lock_value, expire_time, create_time) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "lock_value = CASE " +
                    "  WHEN expire_time < ? THEN VALUES(lock_value) " +
                    "  ELSE lock_value END, " +
                    "expire_time = CASE " +
                    "  WHEN expire_time < ? THEN VALUES(expire_time) " +
                    "  ELSE expire_time END";
        
        try {
            long currentTime = System.currentTimeMillis();
            long expireTimeMillis = currentTime + expireTime;
            
            int updated = jdbcTemplate.update(sql, 
                lockKey, lockValue, expireTimeMillis, currentTime,
                currentTime, currentTime);
            
            if (updated > 0) {
                String checkSql = "SELECT lock_value FROM " + LOCK_TABLE + 
                                " WHERE lock_key = ? AND lock_value = ?";
                String result = jdbcTemplate.queryForObject(checkSql, String.class, lockKey, lockValue);
                
                if (lockValue.equals(result)) {
                    return LockResult.builder()
                        .success(true)
                        .lockValue(lockValue)
                        .build();
                }
            }
            
            return LockResult.builder().success(false).build();
            
        } catch (Exception e) {
            log.error("数据库锁获取失败: lockKey={}", lockKey, e);
            return LockResult.builder().success(false).build();
        }
    }
    
    @Override
    public boolean unlock(String lockKey, String lockValue) {
        String sql = "DELETE FROM " + LOCK_TABLE + 
                    " WHERE lock_key = ? AND lock_value = ?";
        
        try {
            int deleted = jdbcTemplate.update(sql, lockKey, lockValue);
            return deleted > 0;
        } catch (Exception e) {
            log.error("数据库锁释放失败: lockKey={}", lockKey, e);
            return false;
        }
    }
}
```

## 5. 锁管理器

```java
@Component
public class DistributedLockManager {
    
    @Autowired
    private Map<String, LockStrategy> lockStrategies;
    
    @Autowired
    private LockMonitor lockMonitor;
    
    private static final String DEFAULT_STRATEGY = "redis";
    
    /**
     * 尝试获取锁
     */
    public LockResult tryLock(String lockKey, long expireTime) {
        return tryLock(lockKey, DEFAULT_STRATEGY, expireTime);
    }
    
    public LockResult tryLock(String lockKey, String strategy, long expireTime) {
        LockStrategy lockStrategy = lockStrategies.get(strategy);
        if (lockStrategy == null) {
            throw new IllegalArgumentException("不支持的锁策略: " + strategy);
        }
        
        String lockValue = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        
        try {
            LockResult result = lockStrategy.tryLock(lockKey, lockValue, expireTime);
            
            lockMonitor.recordLockAttempt(lockKey, strategy, result.isSuccess(), 
                System.currentTimeMillis() - startTime);
            
            return result;
        } catch (Exception e) {
            lockMonitor.recordLockError(lockKey, strategy, e);
            throw e;
        }
    }
    
    /**
     * 自动锁（推荐使用）
     */
    public <T> T executeWithLock(String lockKey, long expireTime, Supplier<T> supplier) {
        LockResult lockResult = tryLock(lockKey, expireTime);
        if (!lockResult.isSuccess()) {
            throw new LockAcquisitionException("获取锁失败: " + lockKey);
        }
        
        try {
            return supplier.get();
        } finally {
            unlock(lockKey, lockResult.getLockValue());
        }
    }
    
    public void executeWithLock(String lockKey, long expireTime, Runnable runnable) {
        LockResult lockResult = tryLock(lockKey, expireTime);
        if (!lockResult.isSuccess()) {
            throw new LockAcquisitionException("获取锁失败: " + lockKey);
        }
        
        try {
            runnable.run();
        } finally {
            unlock(lockKey, lockResult.getLockValue());
        }
    }
}
```

## 6. 核心模型

### 6.1 锁策略接口
```java
public interface LockStrategy {
    
    /**
     * 尝试获取锁
     */
    LockResult tryLock(String lockKey, String lockValue, long expireTime);
    
    /**
     * 释放锁
     */
    boolean unlock(String lockKey, String lockValue);
    
    /**
     * 检查锁是否存在
     */
    default boolean isLocked(String lockKey) {
        throw new UnsupportedOperationException("不支持的操作");
    }
}
```

### 6.2 锁结果模型
```java
@Data
@Builder
public class LockResult {
    private boolean success;
    private String lockValue;
    private String errorMessage;
    private long acquireTime;
    
    public static LockResult success(String lockValue) {
        return LockResult.builder()
            .success(true)
            .lockValue(lockValue)
            .acquireTime(System.currentTimeMillis())
            .build();
    }
    
    public static LockResult failure(String errorMessage) {
        return LockResult.builder()
            .success(false)
            .errorMessage(errorMessage)
            .build();
    }
}
```

## 7. 使用示例

### 7.1 基础使用
```java
@Service
public class BusinessService {
    
    @Autowired
    private DistributedLockManager lockManager;
    
    public void processWithLock(String businessKey) {
        // 方式1：手动管理锁
        LockResult lockResult = lockManager.tryLock("business:" + businessKey, 30000);
        if (lockResult.isSuccess()) {
            try {
                doBusinessLogic(businessKey);
            } finally {
                lockManager.unlock("business:" + businessKey, lockResult.getLockValue());
            }
        } else {
            throw new RuntimeException("获取锁失败");
        }
    }
    
    public void processWithAutoLock(String businessKey) {
        // 方式2：自动管理锁（推荐）
        lockManager.executeWithLock("business:" + businessKey, 30000, () -> {
            doBusinessLogic(businessKey);
        });
    }
}
```

### 7.2 分布式任务调度
```java
@Service
public class TaskSchedulerService {
    
    @Autowired
    private DistributedLockManager lockManager;
    
    @Scheduled(cron = "0 */5 * * * ?") // 每5分钟执行一次
    public void scheduleTask() {
        String lockKey = "task-scheduler:schedule";
        
        lockManager.executeWithLock(lockKey, 60000, () -> {
            // 只有获得锁的节点才执行任务调度
            List<Task> tasks = findScheduledTasks();
            for (Task task : tasks) {
                submitTask(task);
            }
        });
    }
}
```

## 8. 配置管理

### 8.1 自动配置
```java
@Configuration
@EnableConfigurationProperties(DistributedLockProperties.class)
public class DistributedLockAutoConfiguration {
    
    @Bean
    @ConditionalOnProperty(name = "distributed-lock.redis.enabled", havingValue = "true")
    public LockStrategy redisLockStrategy(StringRedisTemplate redisTemplate) {
        return new RedisLockStrategy(redisTemplate);
    }
    
    @Bean
    @ConditionalOnProperty(name = "distributed-lock.zookeeper.enabled", havingValue = "true")
    public LockStrategy zookeeperLockStrategy(CuratorFramework curatorClient) {
        return new ZookeeperLockStrategy(curatorClient);
    }
    
    @Bean
    @ConditionalOnProperty(name = "distributed-lock.database.enabled", havingValue = "true")
    public LockStrategy databaseLockStrategy(JdbcTemplate jdbcTemplate) {
        return new DatabaseLockStrategy(jdbcTemplate);
    }
    
    @Bean
    public DistributedLockManager distributedLockManager(Map<String, LockStrategy> lockStrategies) {
        return new DistributedLockManager(lockStrategies);
    }
}
```

### 8.2 配置属性
```java
@Data
@ConfigurationProperties(prefix = "distributed-lock")
public class DistributedLockProperties {
    
    private Redis redis = new Redis();
    private Zookeeper zookeeper = new Zookeeper();
    private Database database = new Database();
    
    @Data
    public static class Redis {
        private boolean enabled = true;
        private String prefix = "distributed-lock:";
        private long defaultExpireTime = 30000;
        private int retryTimes = 3;
        private long retryInterval = 100;
    }
    
    @Data
    public static class Zookeeper {
        private boolean enabled = false;
        private String lockPath = "/distributed-locks/";
        private long defaultExpireTime = 30000;
        private int retryTimes = 3;
        private long retryInterval = 100;
    }
    
    @Data
    public static class Database {
        private boolean enabled = false;
        private String tableName = "distributed_lock";
        private long defaultExpireTime = 30000;
        private int retryTimes = 3;
        private long retryInterval = 100;
    }
}
```

## 9. 最佳实践

### 9.1 锁命名规范
```java
public class LockNamingConvention {
    
    /**
     * 业务锁命名规范
     */
    public static String businessLock(String business, String resource, String operation) {
        return String.format("business:%s:%s:%s", business, resource, operation);
    }
    
    /**
     * 用户锁命名规范
     */
    public static String userLock(String userId, String operation) {
        return String.format("user:%s:%s", userId, operation);
    }
    
    /**
     * 资源锁命名规范
     */
    public static String resourceLock(String resourceType, String resourceId) {
        return String.format("resource:%s:%s", resourceType, resourceId);
    }
    
    /**
     * 定时任务锁命名规范
     */
    public static String scheduledTaskLock(String taskName) {
        return String.format("scheduled:%s", taskName);
    }
}
```

### 9.2 锁使用原则
1. **最小化锁范围**：只在必要的代码段使用锁
2. **避免嵌套锁**：防止死锁
3. **合理设置超时**：避免长时间等待
4. **及时释放锁**：使用try-finally确保锁释放
5. **监控锁使用**：及时发现性能问题

## 10. 总结

本分布式锁设计方案提供了完整的分布式锁解决方案，具备以下特点：

1. **多种实现**：支持Redis、Zookeeper、数据库等多种锁实现
2. **高可用性**：具备故障转移和健康检查机制
3. **易用性**：提供简洁的API和自动锁管理
4. **可监控性**：完整的监控和告警机制
5. **高性能**：优化的锁粒度和超时策略
6. **可扩展性**：支持自定义锁策略

## 分布式锁设计方案总结
🎯 核心特性
多种实现策略
Redis分布式锁（基础锁、可重入锁）
Zookeeper分布式锁
数据库分布式锁
统一管理接口
DistributedLockManager：统一的锁管理器
LockStrategy：锁策略接口
支持自动锁管理（推荐使用）
完善的监控体系
LockMonitor：锁监控器
LockMetrics：锁指标模型
LockStatistics：统计信息
LockHealthChecker：健康检查
异常处理机制
LockAcquisitionException：锁获取异常
重试机制
错误记录和告警


## 核心功能
1. 分布式锁管理器 (DistributedLockManager)
统一的锁管理接口
支持自动锁管理（推荐使用）
支持重试机制
支持多种锁策略
2. Redis锁实现 (RedisLockStrategy)
基于Redis SET NX EX命令
支持可重入锁
使用Lua脚本确保原子性
完善的异常处理
3. 锁监控系统
实时监控锁使用情况
性能指标统计
健康检查机制
REST API接口
4. 锁命名规范 (LockNamingConvention)
统一的锁命名规则
支持多种业务场景
防止锁键冲突





该方案可以满足分布式系统中的各种锁需求，为系统提供可靠、高效的分布式锁服务。

```
vivi-redis-lock/
├── pom.xml                                    # Maven配置文件
├── README.md                                  # 完整的设计方案文档
├── src/
│   ├── main/
│   │   ├── java/org/vivi/redis/lock/
│   │   │   ├── DistributedLockManager.java   # 分布式锁管理器
│   │   │   ├── LockStrategy.java             # 锁策略接口
│   │   │   ├── LockResult.java               # 锁结果模型
│   │   │   ├── RedisLockStrategy.java        # Redis锁实现
│   │   │   ├── LockMonitor.java              # 锁监控器
│   │   │   ├── LockMetrics.java              # 锁指标模型
│   │   │   ├── LockStatistics.java           # 锁统计信息
│   │   │   ├── LockAcquisitionException.java # 锁获取异常
│   │   │   ├── LockNamingConvention.java     # 锁命名规范
│   │   │   ├── LockHealthChecker.java        # 健康检查器
│   │   │   ├── config/
│   │   │   │   ├── RedisLockAutoConfiguration.java # 自动配置
│   │   │   │   └── RedisLockProperties.java  # 配置属性
│   │   │   ├── controller/
│   │   │   │   └── LockMonitorController.java # 监控控制器
│   │   │   └── example/
│   │   │       └── LockUsageExample.java     # 使用示例
│   │   └── resources/
│   │       ├── META-INF/spring.factories     # Spring自动配置
│   │       └── application-redis-lock.yml    # 示例配置
│   └── test/
│       ├── java/org/vivi/redis/lock/
│       │   ├── DistributedLockManagerTest.java
│       │   ├── RedisLockStrategyTest.java
│       │   ├── LockNamingConventionTest.java
│       │   └── RedisLockIntegrationTest.java
│       └── resources/
│           └── application-test.yml          # 测试配置
```