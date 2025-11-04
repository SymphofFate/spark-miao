package com.spark.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 19:10
 * @Description
 */
@Data
public class RoleDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色权限
     */
    private String rolePower;
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 逻辑删除
     */
    private int delFlag;
    /**
     * 创建人
     */
    private Long creator;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人
     */
    private Long updater;
    /**
     * 更新时间
     */
    private Date updateDate;
}

