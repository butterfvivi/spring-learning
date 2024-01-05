package org.oauth2.security.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class SysUserController {

    @GetMapping("/me")
    public String getUserLoginInfo() {
        return "me";
    }
}
