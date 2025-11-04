package com.spark.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/2 15:02
 * @Description
 */
@Data
@Builder
public class ChatMessage {
    private Long id;
    private String conversationId;
    private String question;
    private String answer;
    private Boolean useKnowledgeBase;
    private Integer requestCode;
    private Boolean success;
    private String errorMessage;
    private LocalDateTime requestTimestamp;
    private LocalDateTime responseTimestamp;
}
