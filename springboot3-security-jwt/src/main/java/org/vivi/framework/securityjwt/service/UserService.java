package org.vivi.framework.securityjwt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.vivi.framework.securityjwt.mapper.UserMapper;
import org.vivi.framework.securityjwt.model.entity.User;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper userMapper;

    public User getUserInfo(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
        return this.getBaseMapper().selectOne(queryWrapper);
    }
}
