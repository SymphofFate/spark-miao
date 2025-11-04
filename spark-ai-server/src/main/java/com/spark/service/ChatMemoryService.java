package com.spark.service;

import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/2 17:17
 * @Description
 */
public interface ChatMemoryService {

    /**
     * 判断是否存在对话，没有就新建对话
     *
     * @param conversationId
     * @param messages
     */
    void CreateChat(String conversationId, List<Message> messages);

    List<String> findConversationIds(Integer limit);

    List<Message> findByConversationId(String conversationId,Integer limit);

}
