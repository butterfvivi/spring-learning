package org.vivi.framework.iexceltoolkit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iexceltoolkit.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
