package com.spark.repository;

import com.spark.service.ChatMemoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/2 13:39
 * @Description
 */
@Repository
@Log4j2
public class MySQLChatMemoryRepository implements ChatMemoryRepository {

    @Autowired
    private ChatMemoryService service;
    /**
     * 查找会话id
     * @return
     */
    @Override
    public List<String> findConversationIds() {
        return List.of();
    }

    /**
     * 按会话id查找
     * @param conversationId
     * @return
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        return service.findByConversationId(conversationId,10);
    }

    /**
     * 保存所有
     * @param conversationId
     * @param messages
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        service.CreateChat(conversationId,messages);
    }

    /**
     * 按会话id删除
     * @param conversationId
     */
    @Override
    public void deleteByConversationId(String conversationId) {

    }
}
