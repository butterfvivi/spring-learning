package org.spring.cloud.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gateway 路由的两种方式
 * 1、YML配置
 * 2、配置类
 *    通过配置类@Configuration，通过@Bean注入一个RouteLocator方式
 *
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("consumer-service",route ->route.path("/api/consumer/**").
                uri("lb://consumer-service")).build();
        return routes.build();
    }
}
