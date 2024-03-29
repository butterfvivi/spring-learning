package org.spring.oauth2.gateway.config;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.IgnoreUrlsConfig> {
    @Override
    public GatewayFilter apply(AuthGatewayFilterFactory.IgnoreUrlsConfig config) {
        return null;
    }

    @Data
    public static class IgnoreUrlsConfig {

        private List<String> ignoreUrls;
    }
}
