package org.spring.oauth2.resource.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class OAuth2ResourceController {

    @GetMapping("/resource")
    public String home(HttpServletRequest request, Authentication authentication) {
        LocalDateTime time = LocalDateTime.now();
        return "Welcome ResourceServer! - " + time + "<br>" + authentication.getName() + " - " + authentication.getAuthorities();
    }

}
