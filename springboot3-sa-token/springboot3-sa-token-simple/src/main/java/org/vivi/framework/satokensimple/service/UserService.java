package org.vivi.framework.satokensimple.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.satokensimple.controller.dto.LoginOrRegisterDto;
import org.vivi.framework.satokensimple.mapper.UserMapper;
import org.vivi.framework.satokensimple.model.User;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {


    public User getUserInfo(LoginOrRegisterDto registerDto) {
        return this.lambdaQuery()
                .eq(User::getUserName, registerDto.getUserName())
                .one();
    }
}
