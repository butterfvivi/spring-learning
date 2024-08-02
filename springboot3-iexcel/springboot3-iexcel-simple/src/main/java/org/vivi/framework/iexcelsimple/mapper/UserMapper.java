package org.vivi.framework.iexcelsimple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iexcelsimple.entity.dto.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
