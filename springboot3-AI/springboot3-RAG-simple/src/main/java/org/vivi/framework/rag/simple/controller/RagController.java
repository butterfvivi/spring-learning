package org.vivi.framework.rag.simple.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RAG 相关接口
 */
@RestController
@RequestMapping("/ai/rag")
public class RagController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;


    /**
     * 基于 RAG 的问答接口, 根据用户输入的问题，结合向量数据库中的知识进行回答
     * @param userInput 用户输入的问题
     * @return 基于 RAG 模型生成的回答
     */
    @GetMapping(value = "/chat", produces = "text/plain; charset=UTF-8")
    public String generation(String userInput) {
        // 发起聊天请求并处理响应
        return chatClient.prompt()
                .user(userInput)
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .content();
    }
}
