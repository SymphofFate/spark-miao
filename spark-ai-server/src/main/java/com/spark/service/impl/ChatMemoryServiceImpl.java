package com.spark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spark.dao.ChatConversationDao;
import com.spark.dao.ChatMessageDao;
import com.spark.entity.ChatConversation;
import com.spark.entity.ChatMessage;
import com.spark.service.ChatMemoryService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/2 17:19
 * @Description
 */
@Service
public class ChatMemoryServiceImpl implements ChatMemoryService {

    @Resource
    private ChatMessageDao messageDao;

    @Resource
    private ChatConversationDao conversationDao;

    @Override
    public void CreateChat(String conversationId, List<Message> messages) {
        QueryWrapper<ChatConversation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id",conversationId);
        boolean exists = conversationDao.exists(queryWrapper);
        if (!exists){
            conversationDao.insert(ChatConversation.builder()
                    .conversationId(conversationId)
                    .createdTime(LocalDateTime.now()).build());
        }
    }

    @Override
    public List<String> findConversationIds(Integer limit) {
        return List.of();
    }

    @Override
    public List<Message> findByConversationId(String conversationId, Integer limit) {
        QueryWrapper<ChatMessage> wrapper = new QueryWrapper<>();
        wrapper.eq("conversation_id",conversationId).last("LIMIT "+limit);
        List<ChatMessage> messages = messageDao.selectList(wrapper);
        List<Message> list = new ArrayList<>();
        for (ChatMessage message:messages){
            Message message1 = new UserMessage(message.getQuestion());
            Message message2 = new AssistantMessage(message.getAnswer());
            list.add(message1);
            list.add(message2);
        }
        return list;
    }
}
