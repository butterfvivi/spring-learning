package com.example.simple.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(OAuth2AuthenticationToken token){
        // 打印授权用户信息
        System.out.println(token.getPrincipal());
        return "hello";
    }

    @RequestMapping("/sso/callback")
    public String callback(){
        return "hello";
    }

    @RequestMapping("/oauth/redirect")
    public String authorize(){
        return "hello";
    }

    @GetMapping("/test")
    public DefaultOAuth2User test(){
        System.out.println("Test Result~~~~");
        return (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
