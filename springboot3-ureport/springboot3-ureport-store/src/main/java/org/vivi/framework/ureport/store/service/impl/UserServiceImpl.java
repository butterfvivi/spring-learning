package org.vivi.framework.ureport.store.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.vivi.framework.ureport.store.domain.User;
import org.vivi.framework.ureport.store.domain.bo.UserBo;
import org.vivi.framework.ureport.store.mapper.UserMapper;
import org.vivi.framework.ureport.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User selectUserByUserName(UserBo bo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name",bo.getUserName());
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
}
