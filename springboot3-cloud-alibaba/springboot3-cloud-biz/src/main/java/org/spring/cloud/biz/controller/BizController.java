package org.spring.cloud.biz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/biz")
@RestController
public class BizController {

    @GetMapping("/getAll")
    public String getAll() {
        return "hello";
    }

    @PostMapping("/insert")
    public String insert() {
        return "success";
    }
}
