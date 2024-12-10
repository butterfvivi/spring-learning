package org.vivi.framework.sso.server.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.sso.server.domain.model.UserInfo;


@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
