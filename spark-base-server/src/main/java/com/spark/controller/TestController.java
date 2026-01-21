package com.spark.controller;

import com.spark.service.TestService;
import com.spark.utils.*;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource(name = "OrderStateMachine")
    private StateMachine<OrderState, OrderEvent> orderStateMachine;

    @Resource(name = "VoteStateMachine")
    private StateMachine<VoteStateTypeEnum, VoteEventTypeEnum> voteEventTypeEnumStateMachine;

    @Resource
    private TestService service;
    

    @GetMapping("/hello")
    public Result hello() {
        return Result.success("Hello, Spark!");
    }

    @GetMapping("aa")
    public Result test(){
        return Result.success();
    }

    @GetMapping("bb")
    public Result testBb(){
        return Result.success();
    }

    @GetMapping("test")
    public Result statemachineTest(){
        service.statemachineTest();
        return Result.success();
    }


    @GetMapping("vote")
    @Operation(summary = "状态机异步测试")
    public Result voteTest() {
        service.voteStateMachineTest();
        return Result.success();
    }

    @GetMapping("voteTest")
    @Parameter(name = "code",description = "流程码")
    @Operation(summary = "状态机流程测试")
    public Result voteTest(@Param("code") Integer code) {
        return service.voteTest(code);
    }

}
