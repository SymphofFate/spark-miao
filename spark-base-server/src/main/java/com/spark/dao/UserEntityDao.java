package com.spark.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spark.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserEntityDao extends BaseMapper<UserEntity> {
}
