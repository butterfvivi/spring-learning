package org.vivi.framework.password.oauth2.password;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.vivi.framework.password.constant.Oauth2Constant;

import java.util.Map;

public class PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public PasswordGrantAuthenticationToken(Authentication clientPrincipal
            , Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(Oauth2Constant.GRANT_TYPE_PASSWORD), clientPrincipal, additionalParameters);
    }
}
