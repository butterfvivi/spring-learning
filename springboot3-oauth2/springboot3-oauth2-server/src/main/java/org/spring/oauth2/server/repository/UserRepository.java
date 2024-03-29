package org.spring.oauth2.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.spring.oauth2.server.domain.mapper.AuthorityMapper;
import org.spring.oauth2.server.domain.mapper.UserMapper;
import org.spring.oauth2.server.domain.model.Authority;
import org.spring.oauth2.server.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    public User getUserByName(String name) {
        return userMapper.selectOne(new QueryWrapper<User>()
                .eq("name",name)
                .select("id","name","password"));
    }

    public List<Authority> getAuthoritiesByUserId(int userID) {
        return authorityMapper.selectList(new QueryWrapper<Authority>()
                .inSql("id","select authority_id from user_authority where user_id = " + userID)
                .select("id","authority"));
    }

}
