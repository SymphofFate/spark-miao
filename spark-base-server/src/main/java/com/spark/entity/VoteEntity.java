package com.spark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/2 10:44
 * @Description
 */
@Data
@Builder
@TableName("vote")
public class VoteEntity {
    @TableId(type=IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Integer type;
    private String info;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer nature;
    private String delFlag;
    private Long creator;
    private Date createDate;
    private Long updater;
    private Date updateDate;
}
