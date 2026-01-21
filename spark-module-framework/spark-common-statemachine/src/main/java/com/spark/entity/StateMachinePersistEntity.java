package com.spark.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/3 13:37
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateMachinePersistEntity {
    private String id;
    private Integer state;
    private Integer event;
    private Integer eventHead;
}
