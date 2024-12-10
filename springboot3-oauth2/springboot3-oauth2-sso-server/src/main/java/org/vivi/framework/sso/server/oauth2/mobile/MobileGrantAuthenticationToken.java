package org.vivi.framework.sso.server.oauth2.mobile;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.vivi.framework.sso.server.constant.Oauth2Constant;

import java.util.Map;

public class MobileGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    protected MobileGrantAuthenticationToken(Authentication clientPrincipal,
                                             @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(Oauth2Constant.GRANT_TYPE_MOBILE), clientPrincipal, additionalParameters);
    }
}
