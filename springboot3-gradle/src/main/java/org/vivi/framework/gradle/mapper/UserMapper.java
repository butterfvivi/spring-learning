package org.vivi.framework.gradle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.gradle.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
