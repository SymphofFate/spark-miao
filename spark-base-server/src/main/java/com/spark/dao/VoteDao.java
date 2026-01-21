package com.spark.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spark.entity.VoteEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/2 10:55
 * @Description
 */
@Mapper
public interface VoteDao extends BaseMapper<VoteEntity> {
    public void test();
}
