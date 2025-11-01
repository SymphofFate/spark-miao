package com.spark.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 21:29
 * @Description
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "聊天请求参数")
public class ChatRequest {

    private Integer code;
    private String question;
    private String conversationId;
    private Boolean useKnowledgeBase;

}
