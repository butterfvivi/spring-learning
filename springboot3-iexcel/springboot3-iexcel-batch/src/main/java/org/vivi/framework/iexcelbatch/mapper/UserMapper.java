package org.vivi.framework.iexcelbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iexcelbatch.entity.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
