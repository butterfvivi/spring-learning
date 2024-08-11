package org.vivi.framework.postgresql.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.postgresql.model.User;
import org.vivi.framework.postgresql.repository.UserMapper;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    public void insert(){
        userMapper.insert(new User(1L, "test1","123@test.com", 8));
        userMapper.insert(new User(2L, "黄忠","123@test.com", 34));
        userMapper.insert(new User(3L, "张飞","123@test.com", 27));
        userMapper.insert(new User(4L, "貂蝉","123@test.com", 31));
        userMapper.insert(new User(5L, "关羽","123@test.com", 66));
    }

    public void delete(){
        userMapper.deleteById(1);
    }
}
