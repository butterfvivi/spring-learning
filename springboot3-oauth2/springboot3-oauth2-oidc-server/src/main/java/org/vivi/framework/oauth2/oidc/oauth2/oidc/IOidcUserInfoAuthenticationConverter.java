package org.vivi.framework.oauth2.oidc.oauth2.oidc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class IOidcUserInfoAuthenticationConverter implements AuthenticationConverter {

    private IOidcUserInfoService oidcUserInfoService;

    public IOidcUserInfoAuthenticationConverter(IOidcUserInfoService oidcUserInfoService) {
        this.oidcUserInfoService = oidcUserInfoService;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        IOidcUserInfo iOidcUserInfo = oidcUserInfoService.loadUser(authentication.getName());
        return new OidcUserInfoAuthenticationToken(authentication, iOidcUserInfo);
    }
}
