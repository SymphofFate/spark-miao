package com.spark.controller;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 21:34
 * @Description
 */

import com.spark.entity.ChatRequest;
import com.spark.entity.ChatResponse;
import com.spark.entity.KnowledgeDocument;
import com.spark.service.ElasticsearchService;
import com.spark.service.impl.RAGService;
import com.spark.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("chat")
@CrossOrigin
@Tag(name = "聊天管理", description = "提供AI聊天相关的接口服务")
@Slf4j
public class ChatController {

    private static final String DEFAULT_PROMPT = "你要用《崩坏：星穹铁道》中三月七的口吻回答我的问题";

    @Autowired
    private ChatClient dashScopeChatClient;
    @Resource
    private RAGService ragService;
    @Resource
    private ElasticsearchService elasticsearchService;

    /**
     * 普通对话（不使用知识库）
     */
    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody ChatRequest request) {
        log.info("收到对话请求: {}", request.getQuestion());

        return dashScopeChatClient.prompt()
                .user(request.getQuestion())
                .advisors(a -> a.param("conversationId", request.getConversationId()))
                .stream()
                .content();
    }

    /**
     * 基于知识库的智能问答
     */
    @PostMapping("/ask")
    public Result askWithKnowledge(@RequestBody ChatRequest request) {
        log.info("收到知识库问答请求: {}", request.getQuestion());

        try {
            String answer = ragService.askWithKnowledgeBase(
                    request.getCode(),
                    request.getQuestion(),
                    request.getConversationId()
            );

            ChatResponse response = ChatResponse.builder()
                    .question(request.getQuestion())
                    .answer(answer)
                    .conversationId(request.getConversationId())
                    .timestamp(new Date())
                    .success(true)
                    .build();

            return Result.success(response);

        } catch (Exception e) {
            log.error("知识库问答失败: {}", e.getMessage(), e);

            ChatResponse response = ChatResponse.builder()
                    .question(request.getQuestion())
                    .answer("系统繁忙，请稍后重试")
                    .conversationId(request.getConversationId())
                    .timestamp(new Date())
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();

            return Result.success(response);
        }
    }

    /**
     * 初始化知识库（管理接口）
     */
    @PostMapping("/admin/init-knowledge")
    public Result initKnowledgeBase() {
        try {
            elasticsearchService.initKnowledgeIndex();
            return Result.success("知识库初始化成功");
        } catch (Exception e) {
            return Result.failure(502,"知识库初始化失败: " + e.getMessage());
        }
    }

    @Operation(summary = "初始化知识库")
    @PostMapping("init")
    public Result initKnowledgeBaseByList(@RequestBody List<KnowledgeDocument> list){
        elasticsearchService.saveList(list);
        return Result.success();
    }
}