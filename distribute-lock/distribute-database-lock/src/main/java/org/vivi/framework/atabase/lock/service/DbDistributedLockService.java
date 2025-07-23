package org.vivi.framework.atabase.lock.service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class DbDistributedLockService {

    private final long leaseTimeSeconds = 10; // 锁的租期（秒）

    private ScheduledExecutorService scheduler =  Executors.newSingleThreadScheduledExecutor(); // 用于锁续租

    /**
     * 尝试获取锁
     * @param lockName 锁名称
     * @param tryTimeoutMillis 尝试获取锁的总超时时间
     * @return 是否成功获取锁
     */
    public boolean acquireLock(String lockName,String ownerId, long tryTimeoutMillis) {
        long startTime = System.currentTimeMillis();
        long retryInterval = 100; // 每次重试间隔

        while (System.currentTimeMillis() - startTime < tryTimeoutMillis) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springboot3_demo", "root", "wtf0010.")) {
                conn.setAutoCommit(false); // 开启事务

                // 1. 尝试插入锁记录
                String insertSql = "INSERT INTO db_lock (lock_name, owner_id, acquire_time, expire_time, version) " +
                        "VALUES (?, ?, NOW(), ?, 1)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, lockName);
                    ps.setString(2, ownerId);
                    ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusSeconds(leaseTimeSeconds)));
                    int affectedRows = ps.executeUpdate();
                    if (affectedRows > 0) {
                        conn.commit();
                        //startRenewalTask(lockName,ownerId); // 启动续租任务
                        return true; // 成功获取锁
                    }
                } catch (SQLIntegrityConstraintViolationException e) {
                    // 锁已存在，继续尝试抢占或等待
                    // Fallthrough to select and update logic
                }

                // 2. 如果插入失败，检查锁是否过期并尝试抢占
                String selectSql = "SELECT owner_id, expire_time, version FROM db_lock WHERE lock_name = ?";
                try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                    ps.setString(1, lockName);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        LocalDateTime expireTime = rs.getTimestamp("expire_time").toLocalDateTime();
                        String currentOwnerId = rs.getString("owner_id");
                        long currentVersion = rs.getLong("version");

                        if (expireTime.isBefore(LocalDateTime.now())) { // 锁已过期
                            String updateSql = "UPDATE db_lock SET owner_id = ?, acquire_time = NOW(), expire_time = ?, version = ? " +
                                    "WHERE lock_name = ? AND expire_time <= NOW() AND version = ?";
                            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                                updatePs.setString(1, ownerId);
                                updatePs.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusSeconds(leaseTimeSeconds)));
                                updatePs.setLong(3, currentVersion + 1); // 乐观锁版本号递增
                                updatePs.setString(4, lockName);
                                updatePs.setLong(5, currentVersion); // 确保抢占的是我们刚刚查询到的版本
                                int affectedRows = updatePs.executeUpdate();
                                if (affectedRows > 0) {
                                    conn.commit();
                                    //startRenewalTask(lockName,ownerId);
                                    return true; // 成功抢占过期锁
                                } else {
                                    // 抢占失败，可能是其他客户端抢先一步
                                    conn.rollback();
                                }
                            }
                        } else if (currentOwnerId.equals(ownerId)) {
                            // 已经是当前客户端持有的锁，可以考虑实现可重入锁逻辑
                            conn.rollback(); // 回滚
                            //startRenewalTask(lockName,ownerId); // 确保续租任务在运行
                            return true; // 当前客户端已持有锁
                        } else {
                            // 锁未过期，且被其他客户端持有
                            conn.rollback(); // 回滚
                        }
                    } else {
                        // 理论上不会发生，除非其他客户端在select和rollback之间删除了锁
                        conn.rollback();
                    }
                }
                conn.commit(); // 确保事务提交

            } catch (SQLException e) {
                System.err.println("获取锁发生SQL异常: " + e.getMessage());
                // 异常处理，可能回滚事务等
            }

            try {
                Thread.sleep(retryInterval); // 等待重试
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false; // 超时未获取到锁
    }


    /**
     * 释放锁
     * @param lockName 锁名称
     * @return 是否成功释放锁
     */
    public boolean releaseLock(String lockName,String ownerId) {
        stopRenewalTask(lockName); // 停止续租任务

        String deleteSql = "DELETE FROM db_lock WHERE lock_name = ? AND owner_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springboot3_demo", "root", "wtf0010.");
             PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            ps.setString(1, lockName);
            ps.setString(2, ownerId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("释放锁发生SQL异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 为了避免客户端在持有锁期间因为GC、网络延迟等问题导致锁过期被其他客户端抢占，需要一个后台线程定时“续租”锁。
     */
//    private void startRenewalTask(String lockName,String ownerId) {
//        // 实际上需要一个Map来管理不同锁的续租任务，避免重复启动
//        // 伪代码简化：
//        scheduler.scheduleAtFixedRate(() -> {
//            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springboot3_demo", "root", "wtf0010.");
//                PreparedStatement ps = conn.prepareStatement("UPDATE db_lock SET expire_time = ?, version = version + 1 WHERE lock_name = ? AND owner_id = ?")) {
//                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().plusSeconds(leaseTimeSeconds)));
//                ps.setString(2, lockName);
//                ps.setString(3, ownerId);
//                int affectedRows = ps.executeUpdate();
//                if (affectedRows == 0) {
//                    System.out.println("锁 " + lockName + " 已丢失或被抢占，停止续租。");
//                    stopRenewalTask(lockName); // 停止当前锁的续租任务
//                }
//            } catch (SQLException e) {
//                System.err.println("续租锁 " + lockName + " 发生SQL异常: " + e.getMessage());
//                stopRenewalTask(lockName); // 发生异常也停止续租，防止死循环
//            }
//        }, leaseTimeSeconds / 2, leaseTimeSeconds / 2, TimeUnit.SECONDS);
//    }

    private void stopRenewalTask(String lockName) {
        // 实际中需要取消对应的ScheduledFuture
        // scheduler.shutdownNow(); // 这会关闭整个scheduler，不适合多锁场景
        // Map<String, ScheduledFuture<?>> tasks;
        // tasks.get(lockName).cancel(true);
    }
}
