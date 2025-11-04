package com.spark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spark.entity.RoleEntity;
import com.spark.utils.Result;

import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 19:12
 * @Description
 */
public interface RoleService extends IService<RoleEntity> {
    Result page(Map<String, Object> params);
}
