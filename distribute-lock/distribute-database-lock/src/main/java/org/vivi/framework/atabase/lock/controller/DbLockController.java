package org.vivi.framework.atabase.lock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.atabase.lock.service.DbDistributedLockService;

@RestController
@RequestMapping("/db")
public class DbLockController {

    @Autowired
    private DbDistributedLockService dbDistributedLockService;

    @GetMapping("/doLock")
    public String doBusiness(String lockName, String ownerId) {
        boolean locked = false;
        try {
            // 尝试获取锁，超时2秒
            locked = dbDistributedLockService.acquireLock(lockName, ownerId, 2000);
            if (!locked) {
                return "lock failed";
            }
            // 业务逻辑
            try {
                Thread.sleep(2000);
                System.out.println("doing DB business...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "business done";
        } finally {
            if (locked) {
                try {
                    dbDistributedLockService.releaseLock(lockName, ownerId);
                } catch (Exception e) {
                    // 日志记录释放锁失败
                }
            }
        }
    }
    @GetMapping("/lock")
    public String lock(String lockName, String ownerId) {
        //boolean lock = dbDistributedLockService.acquireLock("demo:business", "123", 5000);
        boolean lock = dbDistributedLockService.acquireLock(lockName, ownerId, 5000);
        return lock ? "lock success" : "lock fail";
    }

    @GetMapping("/unlock")
    public String unlock(String lockName, String ownerId) {
        //boolean unlock = dbDistributedLockService.releaseLock("demo:business", "123");
        boolean unlock = dbDistributedLockService.releaseLock(lockName, ownerId);
        return unlock ? "unlock success" : "unlock fail";
    }
}
