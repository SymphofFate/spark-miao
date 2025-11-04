package com.spark.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spark.entity.ChatConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/2 14:57
 * @Description
 */
@Mapper
public interface ChatConversationDao extends BaseMapper<ChatConversation> {
    @Update("UPDATE chat_conversation SET message_count = message_count+1 WHERE conversation_id = #{conversationId}")
    public void accumulationChat(@Param("conversationId") String conversationId);
}
