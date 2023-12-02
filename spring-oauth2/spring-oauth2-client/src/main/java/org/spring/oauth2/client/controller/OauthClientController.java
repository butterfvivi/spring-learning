package org.spring.oauth2.client.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthClientController {

    @RequestMapping(path = "/hello")
    public String demo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return "hello";
    }

}
