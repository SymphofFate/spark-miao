package com.spark.config;

import com.spark.service.McpService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/29 15:16
 * @Description
 */
@Configuration
public class McpServiceConfig {
    @Bean
    @Primary
    public ToolCallbackProvider toolCallbackProvider(McpService mcpService){
        return MethodToolCallbackProvider.builder().toolObjects(mcpService).build();
    }
}
