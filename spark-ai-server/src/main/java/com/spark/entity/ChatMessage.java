package com.spark.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 21:30
 * @Description
 */
@Schema(description = "聊天消息")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Schema(description = "消息唯一标识")
    private String id;

    @Schema(description = "消息角色", example = "user", allowableValues = {"user", "assistant", "system"})
    private String role;

    @Schema(description = "消息内容", required = true, example = "你好，请介绍一下自己")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "消息时间戳", example = "2023-12-21 10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "使用的AI模型名称", example = "siliconflow")
    private String model;

}
