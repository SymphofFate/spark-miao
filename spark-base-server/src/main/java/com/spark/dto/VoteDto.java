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
    @Schema(description = "name")
    private String name;
    @Schema(description = "type")
    private Integer type;
    @Schema(description = "status")
    private Integer status;
    @Schema(description = "info")
    private String info;
    @Schema(description = "startTime")
    private LocalDateTime startTime;
    @Schema(description = "endTime")
    private LocalDateTime endTime;
    @Schema(description = "nature")
    private Integer nature;
}
