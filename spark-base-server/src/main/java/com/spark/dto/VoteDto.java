package com.spark.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/1 14:56
 * @Description
 */
@Data
@Builder
public class VoteDto {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "id", required = true)
    private Long id;
    @Schema(description = "活动名称")
    private String name;
    @Schema(description = "type")
    private Integer type;
    @Schema(description = "活动状态")
    private Integer status;
    @Schema(description = "活动简介")
    private String info;
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    @Schema(description = "nature")
    private Integer nature;
}
