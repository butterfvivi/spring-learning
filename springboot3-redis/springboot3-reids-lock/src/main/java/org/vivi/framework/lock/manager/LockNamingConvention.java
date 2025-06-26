package org.vivi.framework.lock.manager;

import java.util.UUID;

/**
 * 分布式锁命名规范
 * 提供统一的锁命名规则，确保锁键的唯一性和可读性
 */
public class LockNamingConvention {

    private static final String SEPARATOR = ":";

    /**
     * 业务锁命名规范
     * 格式: business:{业务模块}:{资源}:{操作}
     */
    public static String businessLock(String business, String resource, String operation) {
        return String.join(SEPARATOR, "business", business, resource, operation);
    }

    /**
     * 用户锁命名规范
     * 格式: user:{用户ID}:{操作}
     */
    public static String userLock(String userId, String operation) {
        return String.join(SEPARATOR, "user", userId, operation);
    }

    /**
     * 资源锁命名规范
     * 格式: resource:{资源类型}:{资源ID}
     */
    public static String resourceLock(String resourceType, String resourceId) {
        return String.join(SEPARATOR, "resource", resourceType, resourceId);
    }

    /**
     * 定时任务锁命名规范
     * 格式: scheduled:{任务名称}
     */
    public static String scheduledTaskLock(String taskName) {
        return String.join(SEPARATOR, "scheduled", taskName);
    }

    /**
     * 数据锁命名规范
     * 格式: data:{数据类型}:{数据ID}:{操作}
     */
    public static String dataLock(String dataType, String dataId, String operation) {
        return String.join(SEPARATOR, "data", dataType, dataId, operation);
    }

    /**
     * 缓存锁命名规范
     * 格式: cache:{缓存键}
     */
    public static String cacheLock(String cacheKey) {
        return String.join(SEPARATOR, "cache", cacheKey);
    }

    /**
     * 限流锁命名规范
     * 格式: rate-limit:{资源}:{时间窗口}
     */
    public static String rateLimitLock(String resource, String timeWindow) {
        return String.join(SEPARATOR, "rate-limit", resource, timeWindow);
    }

    /**
     * 幂等性锁命名规范
     * 格式: idempotent:{业务}:{唯一标识}
     */
    public static String idempotentLock(String business, String uniqueId) {
        return String.join(SEPARATOR, "idempotent", business, uniqueId);
    }

    /**
     * 分布式锁命名规范
     * 格式: distributed:{业务}:{资源}:{操作}
     */
    public static String distributedLock(String business, String resource, String operation) {
        return String.join(SEPARATOR, "distributed", business, resource, operation);
    }

    /**
     * 集群锁命名规范
     * 格式: cluster:{集群名称}:{操作}
     */
    public static String clusterLock(String clusterName, String operation) {
        return String.join(SEPARATOR, "cluster", clusterName, operation);
    }

    /**
     * 节点锁命名规范
     * 格式: node:{节点ID}:{操作}
     */
    public static String nodeLock(String nodeId, String operation) {
        return String.join(SEPARATOR, "node", nodeId, operation);
    }

    /**
     * 会话锁命名规范
     * 格式: session:{会话ID}:{操作}
     */
    public static String sessionLock(String sessionId, String operation) {
        return String.join(SEPARATOR, "session", sessionId, operation);
    }

    /**
     * 文件锁命名规范
     * 格式: file:{文件路径}:{操作}
     */
    public static String fileLock(String filePath, String operation) {
        // 将文件路径中的特殊字符替换为安全字符
        String safePath = filePath.replaceAll("[^a-zA-Z0-9._-]", "_");
        return String.join(SEPARATOR, "file", safePath, operation);
    }

    /**
     * 数据库锁命名规范
     * 格式: database:{数据库名}:{表名}:{操作}
     */
    public static String databaseLock(String database, String table, String operation) {
        return String.join(SEPARATOR, "database", database, table, operation);
    }

    /**
     * API锁命名规范
     * 格式: api:{接口路径}:{操作}
     */
    public static String apiLock(String apiPath, String operation) {
        // 将API路径中的特殊字符替换为安全字符
        String safePath = apiPath.replaceAll("[^a-zA-Z0-9._/-]", "_");
        return String.join(SEPARATOR, "api", safePath, operation);
    }

    /**
     * 临时锁命名规范
     * 格式: temp:{业务}:{随机ID}
     */
    public static String tempLock(String business) {
        return String.join(SEPARATOR, "temp", business, UUID.randomUUID().toString());
    }

    /**
     * 全局锁命名规范
     * 格式: global:{操作}
     */
    public static String globalLock(String operation) {
        return String.join(SEPARATOR, "global", operation);
    }

    /**
     * 自定义锁命名规范
     * 格式: {前缀}:{自定义部分}
     */
    public static String customLock(String prefix, String... parts) {
        return String.join(SEPARATOR, prefix, String.join(SEPARATOR, parts));
    }

    /**
     * 验证锁键是否合法
     */
    public static boolean isValidLockKey(String lockKey) {
        if (lockKey == null || lockKey.trim().isEmpty()) {
            return false;
        }

        // 检查长度限制
        if (lockKey.length() > 255) {
            return false;
        }

        // 检查是否包含非法字符
        if (lockKey.contains(" ") || lockKey.contains("\t") || lockKey.contains("\n")) {
            return false;
        }

        return true;
    }

    /**
     * 清理锁键中的特殊字符
     */
    public static String sanitizeLockKey(String lockKey) {
        if (lockKey == null) {
            return "";
        }

        // 替换特殊字符为下划线
        return lockKey.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    /**
     * 获取锁键的命名空间
     */
    public static String getNamespace(String lockKey) {
        if (lockKey == null || !lockKey.contains(SEPARATOR)) {
            return "";
        }

        return lockKey.split(SEPARATOR)[0];
    }

    /**
     * 获取锁键的业务标识
     */
    public static String getBusiness(String lockKey) {
        if (lockKey == null || !lockKey.contains(SEPARATOR)) {
            return "";
        }

        String[] parts = lockKey.split(SEPARATOR);
        return parts.length > 1 ? parts[1] : "";
    }

    /**
     * 规范化锁键，去除非法字符
     */
    public static String normalizeKey(String lockKey) {
        return sanitizeLockKey(lockKey);
    }
}