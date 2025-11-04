package com.spark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/3 11:16
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Commodity {
    private String name;
    private Double money;
    private Integer number;
}
