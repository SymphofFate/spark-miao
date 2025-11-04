package com.spark.entity;

import lombok.Data;

import java.util.Date;
@Data
public class User {
    private Long id ;
    private String username ;
    private String email ;
    private String password ;
    private String nickname ;
    private Long tenantId ;
    private String headsUrl;
    private String sign;
    private String delFlag ;
    private Long creator ;
    private Date createDate ;
    private Long updater ;
    private Date updateDate ;
}
