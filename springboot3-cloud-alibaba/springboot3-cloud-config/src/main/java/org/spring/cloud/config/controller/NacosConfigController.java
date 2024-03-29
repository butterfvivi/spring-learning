package org.spring.cloud.config.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NacosConfigController {

    @Value("${config.appName}")
    private String appName;

    @GetMapping("/test1")
    public String nacosConfingTest2() {
        return appName;
    }
}
