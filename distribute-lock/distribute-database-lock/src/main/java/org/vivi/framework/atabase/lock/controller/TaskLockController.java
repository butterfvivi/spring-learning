package org.vivi.framework.atabase.lock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.atabase.lock.service.TaskLockService;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskLockController {

    private final TaskLockService taskLockService;

    public TaskLockController(TaskLockService taskLockService) {
        this.taskLockService = taskLockService;
    }

    @GetMapping("/lock")
    public String doBusiness(long taskId) {
        boolean locked = false;
        try {
            // 尝试获取锁，等待2秒，持有5秒
            locked = taskLockService.tryLock(taskId, 2, 5, TimeUnit.SECONDS);
            if (!locked) {
                return "lock failed";
            }
            try {
                Thread.sleep(10000);
                System.out.println("doing task business...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "business done";
        } finally {
            if (locked) {
                try {
                    taskLockService.unlock(String.valueOf(taskId));
                } catch (Exception e) {
                    // 日志记录释放锁失败
                }
            }
        }
    }

    @RequestMapping("/lock/concurrent")
    public String concurrentLockDemo(long taskId, int threadCount) {
        StringBuilder result = new StringBuilder();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                boolean locked = false;
                try {
                    locked = taskLockService.tryLock(taskId, 2000, 5, java.util.concurrent.TimeUnit.SECONDS);
                    if (locked) {
                        // 模拟业务处理
                        result.append("[Thread-").append(idx).append("] got lock and did business.\n");
                        log.info("[Thread-{}] doing task business...", result);
                        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                    } else {
                        result.append("[Thread-").append(idx).append("] failed to get lock.\n");
                        log.info("[Thread-{}] failed to get lock.", result);
                    }
                } finally {
                    if (locked) {
                        try { taskLockService.unlock(String.valueOf(taskId)); } catch (Exception ignored) {}
                    }
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException ignored) {}
        }
        return result.toString();
    }
}


