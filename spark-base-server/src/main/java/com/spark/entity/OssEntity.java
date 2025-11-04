package com.spark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/23 15:24
 * @Description
 */
@Data
@TableName("t_sys_oss")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OssEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String url;
    private String fileName;
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
