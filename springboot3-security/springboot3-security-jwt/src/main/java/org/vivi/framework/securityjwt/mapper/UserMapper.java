package org.vivi.framework.securityjwt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.securityjwt.model.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
