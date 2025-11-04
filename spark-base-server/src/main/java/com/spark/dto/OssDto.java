package com.spark.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/23 15:26
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OssDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String url;
    private String fileName;
    private int delFlag;
    private Long creator;
    private Date createDate;
    private Long updater;
    private Date updateDate;
}
