package org.vivi.framework.mcp.server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.vivi.framework.mcp.server.service.OpenOrderService;

@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }


    @Bean
    public ToolCallbackProvider orderTools(OpenOrderService openOrderService) {
        return MethodToolCallbackProvider.builder().toolObjects(openOrderService).build();
    }
}
