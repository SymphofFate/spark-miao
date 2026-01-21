package com.spark.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/6 11:38
 * @Description
 */
@Data
@Builder
public class OptionDto {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "id", required = true)
    private Long id;
    @Schema(description = "题目内容")
    private String context;
    @Schema(description = "归属用户")
    private Long affiliationUser;
    @Schema(description = "归属团队")
    private Long affiliationGroup;
}
