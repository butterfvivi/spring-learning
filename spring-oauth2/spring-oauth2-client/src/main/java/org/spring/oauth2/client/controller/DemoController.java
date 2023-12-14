package org.spring.oauth2.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

    @GetMapping("/")
    public String welcome() {

        return "<h1>Welcome!</h1>";
    }

//    @RequestMapping(path = "/hello")
//    public String hello() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        return "hello";
//    }
}
