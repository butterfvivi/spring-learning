package org.vivi.framework.dynamic.simple.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.vivi.framework.dynamic.simple.model.User;

@DS("slave")
public interface UserMapper extends BaseMapper<User> {
}
