package org.spring.oauth2.gateway.config;

import cn.hutool.core.text.AntPathMatcher;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * 自定义授权管理器，判断用户是否有权限访问
 */
@Component
@Slf4j
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final Map<String,String> authMap = Maps.newConcurrentMap();

    public void initAuthMap(){
        authMap.put("","");
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String authorities = authMap.get(path);

        //放行option请求
        if (request.getMethod() == HttpMethod.OPTIONS){
            return Mono.just(new AuthorizationDecision(true));
        }

        if (StringUtils.hasText(authorities)){
            return Mono.just(new AuthorizationDecision(false));
        }
        return authentication
                .filter(Authentication::isAuthenticated)
                .filter(f ->f instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .doOnNext(token -> {
                    log.info("token header:{}",token.getToken().getHeaders());
                    log.info("token info:{}",token.getTokenAttributes());
                })
                .flatMapIterable(AbstractAuthenticationToken::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority -> Objects.equals(authority,authorities))
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false))
                ;
    }
}
