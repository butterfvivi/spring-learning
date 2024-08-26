package org.vivi.framework.websocket.starter.utils;

import cn.hutool.core.collection.CollUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.vivi.framework.websocket.starter.dto.WebSocketMessageDto;
import org.vivi.framework.websocket.starter.holder.WebSocketSessionHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.vivi.framework.websocket.starter.constants.Constants.WEB_SOCKET_TOPIC;

@Slf4j
@NoArgsConstructor
public class WebSocketUtils {

    public static void sendMessage(Long sessionKey,String message) {
        WebSocketSession session = WebSocketSessionHolder.getSession(sessionKey);
    }

    /**
     * 订阅WebSocket消息主题，并提供一个消费者函数来处理接收到的消息
     *
     * @param consumer 处理WebSocket消息的消费者函数
     */
    public static void subscribeMessage(Consumer<WebSocketMessageDto> consumer) {
        //RedisUtils.subscribe(WEB_SOCKET_TOPIC, WebSocketMessageDto.class, consumer);
    }

    public static void publishMessage(WebSocketMessageDto webSocketMessageDto){
        List<Long> unsentSessionKeys = new ArrayList<>();
        //当前服务内session，直接发送消息
        for (Long sessionKey : webSocketMessageDto.getSessionKeys()) {
            if (WebSocketSessionHolder.existSession(sessionKey)){
                WebSocketUtils.sendMessage(sessionKey,webSocketMessageDto.getMessage());
                continue;
            }
            unsentSessionKeys.add(sessionKey);
        }
        if (CollUtil.isNotEmpty(unsentSessionKeys)){
            WebSocketMessageDto broadcastMessage = new WebSocketMessageDto();
            broadcastMessage.setMessage(webSocketMessageDto.getMessage());
            broadcastMessage.setSessionKeys(unsentSessionKeys);
//            RedisUtils.publish(WEB_SOCKET_TOPIC, broadcastMessage, consumer -> {
//                log.info("WebSocket发送主题订阅消息topic:{} session keys:{} message:{}",
//                        WEB_SOCKET_TOPIC, unsentSessionKeys, webSocketMessageDto.getMessage());
//            });
        }
    }

    /**
     * 向所有的WebSocket会话发布订阅的消息(群发)
     * @param message 要发布的消息内容
     */
    public static void publishAll(String message){
        WebSocketMessageDto webSocketMessageDto = new WebSocketMessageDto();
        webSocketMessageDto.setMessage(message);
//        RedisUtils.publish(WEB_SOCKET_TOPIC, broadcastMessage, consumer -> {
//            log.info("WebSocket发送主题订阅消息topic:{} message:{}", WEB_SOCKET_TOPIC, message);
//        });
    }

    /**
     * 向指定的WebSocket会话发送Pong消息
     * @param session 要发送Pong消息的WebSocket会话
     */
    public static void sendPongMessage(WebSocketSession session) {
        sendMessage(session,new PongMessage());
    }

    /**
     * 向指定的WebSocket会话发送文本消息
     * @param session WebSocket会话
     * @param message 要发送的文本消息内容
     */
    public static void sendMessage(WebSocketSession session,String message) {
        sendMessage(session,new TextMessage(message));
    }

    /**
     * 向指定的WebSocket会话发送WebSocket消息对象
     *
     * @param session WebSocket会话
     * @param message 要发送的WebSocket消息对象
     */
    public static void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (session == null || !session.isOpen()){
            log.warn("[send] session会话已经关闭");
        }else {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.error("[send] session({}) 发送消息({}) 异常", session, message, e);
            }
        }
    }
}
