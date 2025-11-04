package com.spark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/2 21:29
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyMessage implements Message{

    private String type;
    private String content;


    @Override
    public MessageType getMessageType() {
        return MessageType.fromValue(type);
    }

    @Override
    public String getText() {
        return content;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return Map.of();
    }
}
