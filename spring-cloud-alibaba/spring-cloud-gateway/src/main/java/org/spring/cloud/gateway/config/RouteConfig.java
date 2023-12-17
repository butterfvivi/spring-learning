package org.spring.cloud.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLs(RouteLocatorBuilder builder){
        return builder.routes()
                .route("consumer-service",predicateSpec -> predicateSpec.path("/api/consumer/requestRoutes")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.modifyRequestBody(String.class, Map.class
                        , MediaType.APPLICATION_JSON_VALUE,(exchange, s) -> {
                                    Map<String, Object> params = new HashMap<>(16);
                                    params.put("old", s);
                                    params.put("username", "v_huan");
                                    params.put("roles", "ROLE_ADMIN");
                                    return Mono.just(params);
                                })).uri("lb://consumer-service")
                )
                .build();
    }

}
