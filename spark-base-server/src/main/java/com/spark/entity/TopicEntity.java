package com.spark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/6 10:55
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("topic")
public class TopicEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String context;
    private Long affiliationUser;
    private Long affiliationGroup;
    private String delFlag;
    private Long creator;
    private Date createDate;
    private Long updater;
    private Date updateDate;
}
