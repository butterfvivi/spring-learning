package org.vivi.framework.ai.simple.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础AI api的接口调用
 */
@RestController
@RequestMapping("ai")
public class AiController {

    private final ChatClient chatClient;

    public AiController(ChatClient.Builder builder) {
        this.chatClient =  builder
                .defaultSystem("你是一个友好的聊天机器人，回答问题时要使用{voice}的语气")
                .build();
    }

    @GetMapping("api")
    public String generate(@RequestParam(value = "message", defaultValue = "说一个笑话") String message, String voice) {
        return this.chatClient.prompt()
                .user( message)
                .call()
                .content();
    }
}
