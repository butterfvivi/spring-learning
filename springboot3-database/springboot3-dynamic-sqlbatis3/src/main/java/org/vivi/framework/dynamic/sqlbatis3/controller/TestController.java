package org.vivi.framework.dynamic.sqlbatis3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.dynamic.sqlbatis3.mapper.UserMapper;

import java.time.LocalDateTime;

@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test")
    public void test() {
        userMapper.findListOrg("", LocalDateTime.now().plusHours(1L));
    }
}
