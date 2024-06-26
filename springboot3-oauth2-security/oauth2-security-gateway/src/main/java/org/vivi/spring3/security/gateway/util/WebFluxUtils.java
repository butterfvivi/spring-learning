package org.vivi.spring3.security.gateway.util;

import cn.hutool.json.JSONUtil;
import org.vivi.spring3.security.gateway.common.Result;
import org.vivi.spring3.security.gateway.common.ResultCode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * webflux工具类
 *
 * @author haoxr
 * @date 2022/8/28
 */
public class WebFluxUtils {
    public static Mono<Void> writeResponse(ServerHttpResponse response, ResultCode resultCode) {
        switch (resultCode) {
            case ACCESS_UNAUTHORIZED:
            case INVALID_TOKEN:
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                break;
            case TOKEN_ACCESS_FORBIDDEN:
                response.setStatusCode(HttpStatus.FORBIDDEN);
                break;
            default:
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                break;
        }
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
        String body = JSONUtil.toJsonStr(Result.failed(resultCode));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
    }
}
