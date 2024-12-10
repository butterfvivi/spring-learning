package org.vivi.framework.sso.server.oauth2.oidc;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.vivi.framework.sso.server.domain.model.UserInfo;
import org.vivi.framework.sso.server.service.UserService;

import java.util.Map;

@Service
public class IOidcUserInfoService {

    @Resource
    private UserService userService;

    public IOidcUserInfo loadUser(String username){
        UserInfo userInfo = userService.getUserByUsername(username);
        return new IOidcUserInfo(this.createUser(userInfo));
    }

    private Map<String, Object> createUser(UserInfo userInfo) {
        return IOidcUserInfo.myBuilder()
                .name(userInfo.getName())
                .username(userInfo.getUsername())
                .description(userInfo.getDescription())
                .status(userInfo.getStatus())
                .phoneNumber(userInfo.getUsername())
                .email(userInfo.getUsername() + "@qq.com")
                .profile("https://biadu.com/" + userInfo.getName())
                .address("xxxx")
                .build()
                .getClaims();
    }
}
