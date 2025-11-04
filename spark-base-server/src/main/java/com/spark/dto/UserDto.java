package com.spark.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "id", required = true)
    private Long id;
    @Schema(description = "账号", required = false)
    private String username;
    @Schema(description = "邮箱", required = false)
    private String email;
    @Schema(description = "昵称", required = false)
    private String nickname;
    @Schema(description = "租户id", required = false)
    private Long tenantId;
    @Schema(description = "逻辑删除", required = false)
    private String delFlag;
    @Schema(description = "新密码，改密码使用", required = false)
    private String newPass;
    private String password;
    @Schema(description = "头像", required = false)
    private String headsUrl;
    @Schema(description = "签名", required = false)
    private String sign;
}
