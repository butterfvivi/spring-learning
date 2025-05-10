package org.vivi.framework.iasync.thread.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.vivi.framework.iasync.thread.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    
}