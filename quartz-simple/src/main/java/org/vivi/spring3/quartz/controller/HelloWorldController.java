package org.vivi.spring3.quartz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

    @RequestMapping("/")
    public String hello(){
        return "hello world";
    }
}
