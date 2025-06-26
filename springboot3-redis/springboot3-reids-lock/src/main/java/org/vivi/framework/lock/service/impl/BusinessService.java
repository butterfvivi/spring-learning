package org.vivi.framework.lock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.lock.manager.DistributedLockManager;

@Service
public class BusinessService {
    
    @Autowired
    private DistributedLockManager lockManager;
    
    // 推荐使用方式：自动管理锁
    public void processWithLock(String businessKey) {
        lockManager.executeWithLock("business:" + businessKey, 30000, () -> {
            // 执行业务逻辑
            doBusinessLogic(businessKey);
        });
    }
    
    // 带返回值的锁执行
    public String processWithReturn(String businessKey) {
        return lockManager.executeWithLock("business:" + businessKey, 30000, () -> {
            return doBusinessLogicWithReturn(businessKey);
        });
    }
}