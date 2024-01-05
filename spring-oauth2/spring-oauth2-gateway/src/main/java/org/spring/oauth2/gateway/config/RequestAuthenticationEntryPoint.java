package org.spring.oauth2.gateway.config;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * token认证失败、或者超时处理
 * 认证失败异常处理
 */
public class RequestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(serverHttpResponse -> {
                    serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                    String body = "{\"code\":401,\"msg\":\"token不合法或过期\"}";
                    DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                    return serverHttpResponse.writeWith(Mono.just(dataBuffer))
                            .doOnError(error -> DataBufferUtils.release(dataBuffer));
                });
    }
}
