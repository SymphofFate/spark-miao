package com.spark.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.spark.utils.SystemTypeEnum;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory; // 或其他ChatMemory实现
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    // 1. 配置ChatMemoryRepository (消息存储)
    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        // 示例使用内存存储，生产环境可替换为JdbcChatMemoryRepository等持久化方案
        return new InMemoryChatMemoryRepository();
    }

    // 2. 配置ChatMemory (记忆策略)
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        // 使用基于消息窗口的记忆策略，保留最近50条交互消息
        // 你可以根据需要选择不同的ChatMemory实现，例如TokenWindowChatMemory
        return MessageWindowChatMemory.builder().chatMemoryRepository(chatMemoryRepository).maxMessages(10).build();
    }

    // 3. 配置ChatClient，使用正确的Advisor
    @Bean
    public ChatClient dashScopeChatClient(ChatClient.Builder chatClientBuilder,
                                          ChatMemory chatMemory,
                                          ToolCallbackProvider toolCallbackProvider) {
        return chatClientBuilder
                //预设身份
                .defaultSystem(SystemTypeEnum.阿米娅.getSystemPrompt())
                // 使用MessageChatMemoryAdvisor提供对话记忆功能
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                //使用自定义工具
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }
}
