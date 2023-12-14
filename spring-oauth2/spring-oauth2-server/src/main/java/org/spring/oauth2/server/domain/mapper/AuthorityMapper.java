package org.spring.oauth2.server.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.spring.oauth2.server.domain.model.Authority;

@Mapper
public interface AuthorityMapper extends BaseMapper<Authority> {
}
