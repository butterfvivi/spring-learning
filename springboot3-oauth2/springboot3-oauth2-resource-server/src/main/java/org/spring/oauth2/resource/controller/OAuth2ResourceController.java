package org.spring.oauth2.resource.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class OAuth2ResourceController {

//    @GetMapping("/")
//    public String home() {
//        LocalDateTime time = LocalDateTime.now();
//        return "Welcome Resource Server! - " + time;
//    }
//
//    @GetMapping("/")
//    public String home(Authentication authentication) {
//        LocalDateTime time = LocalDateTime.now();
//        return "Welcome ResourceServer! - " + time + "<br>" + authentication.getName() + " - " + authentication.getAuthorities();
//    }

    @GetMapping("/messages1")
    public String getMessages1() {
        return " hello Message 1";
    }

    @GetMapping("/messages2")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public String getMessages2() {
        return " hello Message 2";
    }

    @GetMapping("/messages3")
    @PreAuthorize("hasAuthority('SCOPE_Message')")
    public String getMessages3() {
        return " hello Message 3";
    }

}
