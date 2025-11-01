package com.spark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_base_user")
public class UserEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String nickname;
    private Long tenantId;
    private String headsUrl;
    private String sign;
    private String delFlag;
    private Long creator;
    private Date createDate;
    private Long updater;
    private Date updateDate;
}
