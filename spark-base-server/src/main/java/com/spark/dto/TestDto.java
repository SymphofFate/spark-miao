package com.spark.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2026/1/23 17:21
 * @Description
 */
@Data
@Builder
public class TestDto {
    private Long id;
    private String test;
}
