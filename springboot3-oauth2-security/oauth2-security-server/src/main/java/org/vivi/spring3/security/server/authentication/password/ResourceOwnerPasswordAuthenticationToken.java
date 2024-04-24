package org.vivi.spring3.security.server.authentication.password;

import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResourceOwnerPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private final Set<String> scopes;

    /**
     * {@link  org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken}
     *
     * @param clientPrincipal
     * @param additionalParameters
     */
    protected ResourceOwnerPasswordAuthenticationToken(
            Authentication clientPrincipal,
            @Nullable Set<String> scopes,
            Map<String, Object> additionalParameters
    ) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters);
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());

    }

    public Set<String> getScopes() {
        return this.scopes;
    }

    @Override
    public Object getCredentials() {
        return this.getAdditionalParameters().get(OAuth2ParameterNames.PASSWORD);
    }

}
