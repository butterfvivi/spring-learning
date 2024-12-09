package org.vivi.framework.oauth2.token.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.oauth2.token.domain.model.UserInfo;


@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
