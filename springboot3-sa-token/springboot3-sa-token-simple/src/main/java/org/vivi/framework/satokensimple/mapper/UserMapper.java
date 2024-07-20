package org.vivi.framework.satokensimple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.satokensimple.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
