package org.vivi.framework.websocket.starter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static org.vivi.framework.websocket.starter.constants.Constants.LOGIN_USER_KEY;

/**
 * websocket握手请求的拦截器
 */
@Slf4j
public class PlusWebSocketInterceptor implements HandshakeInterceptor {

    /**
     * websocket 握手之前执行的前置处理方法
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            //LoginUser loginUser = LoginHelper.getLoginUser();
            String loginUser = "admin";
            attributes.put(LOGIN_USER_KEY, loginUser);
            return true;
        } catch (Exception e) {
            log.error("WebSocket 认证失败'{}',无法访问系统资源", e.getMessage());
            return false;
        }
    }

    /**
     * websocket握手成功后执行的后置处理方法
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
