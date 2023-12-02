package org.spring.cloud.biz.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/biz")
@RestController
public class BizController {

    @GetMapping("/")
    public String demo1(HttpServletRequest request) {
        return "hello";
    }

    @GetMapping("/user")
    public String demo2() {
        return "user";
    }
}
