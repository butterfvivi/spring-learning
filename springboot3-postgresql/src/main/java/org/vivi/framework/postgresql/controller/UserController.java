package org.vivi.framework.postgresql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.postgresql.service.UserService;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public void insert(){
        userService.insert();
    }

    @DeleteMapping("/delete")
    public void delete(){
        userService.delete();
    }
}
