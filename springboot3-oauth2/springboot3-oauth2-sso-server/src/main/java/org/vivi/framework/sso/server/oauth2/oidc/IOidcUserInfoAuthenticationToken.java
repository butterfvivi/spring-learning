package org.vivi.framework.sso.server.oauth2.oidc;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.util.SpringAuthorizationServerVersion;
import org.springframework.util.Assert;

public class IOidcUserInfoAuthenticationToken  extends OidcUserInfoAuthenticationToken {

    private static final long serialVersionUID;
    private final Authentication principal;
    private final IOidcUserInfo userInfo;

    static {
        serialVersionUID = SpringAuthorizationServerVersion.SERIAL_VERSION_UID;
    }

    public IOidcUserInfoAuthenticationToken(Authentication principal, IOidcUserInfo userInfo) {
        super(principal,userInfo);
        Assert.notNull(principal, "principal cannot be null");
        Assert.notNull(userInfo, "userInfo cannot be null");
        this.principal = principal;
        this.userInfo = userInfo;
        this.setAuthenticated(true);
    }

    @Override
    public Authentication getPrincipal() {
        return principal;
    }

    @Override
    public IOidcUserInfo getUserInfo() {
        return userInfo;
    }
}
