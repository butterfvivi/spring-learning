package org.vivi.framework.lock.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vivi.framework.lock.manager.DistributedLockManager;
import org.vivi.framework.lock.model.LockResult;

@Component
public class LockExample {
    @Autowired
    private DistributedLockManager lockManager;

    public void doBusinessWithLock() {
        String lockKey = "demo:business";
        LockResult result = lockManager.tryLock(lockKey, 5);
        if (result.isSuccess()) {
            try {
                // 业务逻辑
                System.out.println("业务处理中...");
            } finally {
                lockManager.unlock(lockKey, result.getLockValue());
            }
        } else {
            System.out.println("获取锁失败: " + result.getErrorMessage());
        }
    }
}