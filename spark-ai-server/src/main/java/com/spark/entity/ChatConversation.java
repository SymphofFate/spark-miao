package com.spark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("chat_conversation")
public class ChatConversation {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "消息唯一标识")
    private String conversationId;

    @Schema(description = "创建时间", example = "2023-12-21 10:30:00")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间", example = "2023-12-21 10:30:00")
    private LocalDateTime updatedTime;

    @Schema(description = "消息计数器")
    @TableField("message_count")
    private Integer count;

}
