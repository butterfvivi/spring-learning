package org.spring.oauth2.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.spring.oauth2.server.domain.mapper.UserMapper;
import org.spring.oauth2.server.domain.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private UserMapper userMapper;


    public UserInfo getUserByName(String name) {
        return userMapper.selectOne(new QueryWrapper<UserInfo>()
                .eq("username",name)
                .select("id","username","password"));
    }

}
