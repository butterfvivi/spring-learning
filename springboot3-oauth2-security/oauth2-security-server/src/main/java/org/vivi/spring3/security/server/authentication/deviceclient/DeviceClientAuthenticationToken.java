package org.vivi.spring3.security.server.authentication.deviceclient;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Map;

/**
 * @author haoxr
 * @since 2023/5/17
 */
public class DeviceClientAuthenticationToken extends OAuth2ClientAuthenticationToken {


    public DeviceClientAuthenticationToken(String clientId, ClientAuthenticationMethod clientAuthenticationMethod,
                                           @Nullable Object credentials, @Nullable Map<String, Object> additionalParameters) {
        super(clientId, clientAuthenticationMethod, credentials, additionalParameters);
    }

    public DeviceClientAuthenticationToken(RegisteredClient registeredClient, ClientAuthenticationMethod clientAuthenticationMethod,
                                           @Nullable Object credentials) {
        super(registeredClient, clientAuthenticationMethod, credentials);
    }
}
