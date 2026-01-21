package com.spark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spark.dto.VoteDto;
import com.spark.entity.VoteEntity;
import com.spark.utils.Result;

import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/2 10:58
 * @Description
 */
public interface VoteService extends IService<VoteEntity> {

    /// 创建活动
    public Result create(VoteDto dto);
    /// 开始活动
    public Result start(Long id);
    /// 暂停活动
    public Result stop(Long id);
    /// 结束活动
    public Result end(Long id);

    public Result page(Map<String,Object> map);

    Result info(Long id);
}
