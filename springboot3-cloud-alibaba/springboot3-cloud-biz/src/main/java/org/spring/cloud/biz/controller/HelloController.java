package org.spring.cloud.biz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/biz")
@RestController
public class HelloController {

    @GetMapping("/user")
    public String getUser() {
        return "hello Vivi";
    }

}
