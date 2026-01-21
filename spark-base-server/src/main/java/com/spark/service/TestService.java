package com.spark.service;

import com.spark.utils.Result;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/29 14:24
 * @Description
 */
public interface TestService {
    public void statemachineTest();

    public void voteStateMachineTest();

    public Result voteTest(Integer code);
}
