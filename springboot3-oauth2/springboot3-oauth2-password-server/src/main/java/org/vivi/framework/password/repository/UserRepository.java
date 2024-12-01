package org.vivi.framework.password.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.vivi.framework.password.domain.mapper.UserMapper;
import org.vivi.framework.password.domain.model.UserInfo;

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
