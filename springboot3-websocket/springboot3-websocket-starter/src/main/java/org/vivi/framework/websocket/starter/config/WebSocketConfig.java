package org.vivi.framework.websocket.starter.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.vivi.framework.websocket.starter.handler.PlusWebSocketHandler;
import org.vivi.framework.websocket.starter.interceptor.PlusWebSocketInterceptor;
import org.vivi.framework.websocket.starter.listener.WebSocketTopticListener;
import org.vivi.framework.websocket.starter.properties.WebSocketProperties;

@AutoConfiguration
@ConditionalOnProperty(value = "websocket.enabled", havingValue = "true")
@EnableConfigurationProperties(WebSocketProperties.class)
@EnableWebSocket
public class WebSocketConfig {

    public WebSocketConfigurer  webSocketConfigurer(HandshakeInterceptor handshakeInterceptor,
                                                    WebSocketHandler webSocketHandler, WebSocketProperties webSocketProperties) {
        //如果Websocket的路径为空
        if (StrUtil.isBlank(webSocketProperties.getPath())){
            webSocketProperties.setPath("/websocket");
        }
        //如果允许跨域访问的地址为空，则设置为"*"，表示允许所有来源的跨域请求
        if (StrUtil.isBlank(webSocketProperties.getAllowedOrigins())){
            webSocketProperties.setAllowedOrigins("*");
        }
        //返回一个webSocketConfigurer对象，用于配置websocket
        return registry -> registry
                .addHandler(webSocketHandler,webSocketProperties.getPath())
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins());
    }

    @Bean
    public HandshakeInterceptor handshakeInterceptor(){
        return new PlusWebSocketInterceptor();
    }

    @Bean
    public WebSocketHandler webSocketHandler(){
        return new PlusWebSocketHandler();
    }

    @Bean
    public WebSocketTopticListener webSocketTopticListener(){
        return new WebSocketTopticListener();
    }
}
