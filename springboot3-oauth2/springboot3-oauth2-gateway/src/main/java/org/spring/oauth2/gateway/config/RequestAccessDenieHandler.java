package org.spring.oauth2.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 用户无权限处理
 */
@Slf4j
@Component
public class RequestAccessDenieHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        ServerHttpRequest request = exchange.getRequest();
        return exchange.getPrincipal()
                .doOnNext(principal -> log.info("用户:{}没有访问:{}的权限",principal.getName(),request.getURI()))
                .flatMap(principal -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    String body = "{\"code\":403,\"msg\":\"您无权限访问\"}";
                    DataBuffer dataBuffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(dataBuffer))
                            .doOnError(error-> DataBufferUtils.release(dataBuffer));
                });
    }
}
