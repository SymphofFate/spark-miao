package com.spark.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spark.dto.ChatRequest;
import com.spark.dto.ChatResponse;
import com.spark.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/2 14:32
 * @Description
 */
@Mapper
public interface ChatMessageDao extends BaseMapper<ChatMessage> {
}
