package com.spark.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spark.entity.TopicEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/6 11:30
 * @Description
 */
@Mapper
public interface TopicDao extends BaseMapper<TopicEntity> {
}
