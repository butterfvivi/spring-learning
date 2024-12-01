package org.vivi.framework.password.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.password.domain.model.UserInfo;


@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
