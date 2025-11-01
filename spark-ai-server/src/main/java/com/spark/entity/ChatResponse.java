package com.spark.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 21:31
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@Schema(description = "聊天响应结果")
public class ChatResponse {
    private String question;
    private String answer;
    private String conversationId;
    private Date timestamp;
    private Boolean success;
    private String errorMessage;
}
