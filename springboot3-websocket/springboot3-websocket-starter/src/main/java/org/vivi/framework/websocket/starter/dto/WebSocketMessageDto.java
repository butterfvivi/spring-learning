package org.vivi.framework.websocket.starter.dto;

import lombok.Data;

import java.util.List;

@Data
public class WebSocketMessageDto {

    /**
     * 需要推送到的session key 列表
     */
    private List<Long> sessionKeys;

    /**
     * 需要发送的消息
     */
    private String message;
}
