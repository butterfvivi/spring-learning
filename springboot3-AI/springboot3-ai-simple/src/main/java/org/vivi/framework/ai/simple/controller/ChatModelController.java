package org.vivi.framework.ai.simple.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * 对话模型 chat Model
 * 使用模型(model) 做信息的信息的生成
 * 对话模型（Chat Model）接收一系列消息（Message）作为输入，与模型 LLM 服务进行交互，并接收返回的聊天消息（Chat Message）作为输出。
 * 相比于普通的程序输入，模型的输入与输出消息（Message）不止支持纯字符文本，还支持包括语音、图片、视频等作为输入输出。
 */
@RestController
@RequestMapping("/chatModel")
public class ChatModelController {

    private final ChatModel chatModel;

    public ChatModelController(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @RequestMapping("/chat2")
    public String chat2(String input) {

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withTemperature(0.9)
                .withMaxToken(1500)
                //     .withTopP(0.01)
                .build();

        Prompt prompt = new Prompt(input, options);
        ChatResponse response = chatModel.call(prompt);
        //ChatResponse response = chatModel.call(new Prompt(input));
        return response.getResult().getOutput().getText();
    }


    @RequestMapping("/streamChat")
    public Flux<String> streamChat(String input, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        return chatModel.stream(input);
    }
}
