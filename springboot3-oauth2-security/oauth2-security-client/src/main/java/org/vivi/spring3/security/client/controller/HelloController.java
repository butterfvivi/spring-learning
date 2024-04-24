package org.vivi.spring3.security.client.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String demo(){
        return "hello world";
    }

//    @GetMapping("/hello")
//    public String hello(@AuthenticationPrincipal OAuth2User principal){
//        return "hello, " + principal.getName();
//    }

    @GetMapping("/hello")
    public String hello(Principal principal){
        return "hello, " + principal.getName();
    }

    @RequestMapping("/oauth2/authorization/gitee")
    @ResponseBody
    public String code(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        return code;
    }

    @GetMapping("/user_info")
    public Object userInfo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
