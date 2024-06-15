package org.vivi.framework.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.database.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
