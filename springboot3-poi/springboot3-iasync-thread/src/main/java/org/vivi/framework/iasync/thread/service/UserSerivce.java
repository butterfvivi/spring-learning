package org.vivi.framework.iasync.thread.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.iasync.thread.dto.UserExportTaskDto;
import org.vivi.framework.iasync.thread.entity.UserEntity;

/**
 * 用户服务
 */
public interface UserSerivce extends IService<UserEntity> {

    void exportList(UserExportTaskDto userExportTaskDto);
    
    void exportListByAnnotation(UserExportTaskDto userExportTaskDto);
}
