package org.spring.cloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器 GlobalFIlter
 * 处理一切进入网关的请求和微服务响应
 *
 * 请求进入网关会碰到三类过滤器：当前路由的过滤器、DefaultFilter、GlobalFilter
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst("authorization");

        //String path = exchange.getRequest().getPath().toString();
        log.info("进去了全局过滤器, path:[{}]" , authorization);
        if (authorization == null){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //ServerHttpRequest request = exchange.getRequest().mutate().header("x-token", "123456").build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
