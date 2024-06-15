package org.vivi.framework.database.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.database.mapper.UserMapper;
import org.vivi.framework.database.model.User;

@RestController
@RequestMapping
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("query")
    public User query(Long id) {
        User user = userMapper.selectById(id);
        return user;
    }
}
