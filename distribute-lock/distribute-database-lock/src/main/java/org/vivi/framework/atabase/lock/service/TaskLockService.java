package org.vivi.framework.atabase.lock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vivi.framework.atabase.lock.common.ServiceException;
import org.vivi.framework.atabase.lock.mapper.LockMapper;
import org.vivi.framework.atabase.lock.model.TaskLock;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
public class TaskLockService {

    private final LockMapper taskLockMapper;
    private final String nodeId; // 当前节点标识

    public TaskLockService(LockMapper taskLockMapper) {
        this.taskLockMapper = taskLockMapper;
        this.nodeId = generateNodeId();
    }

    /**
     * 尝试获取任务锁
     * @param taskId 任务ID
     * @param waitTime 等待时间
     * @param leaseTime 锁持有时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    @Transactional
    public boolean tryLock(long taskId, long waitTime, long leaseTime, TimeUnit unit) {
        // 1. 获取当前锁状态（带行锁）
        TaskLock currentLock = taskLockMapper.selectForUpdate(taskId);

        // 2. 检查锁是否可用
        if (currentLock == null) {
            // 锁记录不存在，尝试创建
            TaskLock newLock = new TaskLock();
            newLock.setLockKey(taskId);
            newLock.setOwnerNode(nodeId);
            newLock.setExpireTime(calculateExpireTime(leaseTime, TimeUnit.SECONDS));

            int result = taskLockMapper.insert(newLock);
            if (result == 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * 释放任务锁
     */
    @Transactional
    public void unlock(String taskId) {
        int released = taskLockMapper.releaseLock(taskId, nodeId);
        if (released == 0) {
            throw new ServiceException("500","Failed to release lock, maybe not owned by current node");
        }
    }

    private LocalDateTime calculateExpireTime(long leaseTime, TimeUnit unit) {
        return LocalDateTime.now().plus(unit.toMillis(leaseTime), ChronoUnit.MILLIS);
    }

    private long calculateBackoff(int attempt) {
        return Math.min(1000, 100 * (long) Math.pow(2, attempt));
    }

    private String generateNodeId() {
        return System.getProperty("node.id",
                "node-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000));
    }
}
