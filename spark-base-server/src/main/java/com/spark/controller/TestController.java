package com.spark.controller;

import com.spark.utils.*;
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
        orderStateMachine.start();
        // 当前状态应该是CREATED
        // 发送支付事件，期望状态变为PAID
        boolean payResult = orderStateMachine.sendEvent(OrderEvent.PAY);
        System.out.println("支付事件结果：" + payResult);
//        // 发送发货事件，期望状态变为SHIPPED
//        boolean deliverResult = orderStateMachine.sendEvent(OrderEvent.DELIVER);
//        System.out.println("发货事件结果：" + deliverResult);
//        // 发送收货事件，期望状态变为COMPLETED
//        boolean receiveResult = orderStateMachine.sendEvent(OrderEvent.RECEIVE);
//        System.out.println("收货事件结果：" + receiveResult);

        return Result.success();
    }


    @GetMapping("vote")
    public Result voteTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            voteEventTypeEnumStateMachine.start();
            try {
                voteEventTypeEnumStateMachine.sendEvent(VoteEventTypeEnum.创建活动);
                Thread.sleep(1000);
                voteEventTypeEnumStateMachine.sendEvent(VoteEventTypeEnum.开始活动);
                Thread.sleep(1000);
                voteEventTypeEnumStateMachine.sendEvent(VoteEventTypeEnum.暂停活动);
                Thread.sleep(1000);
                voteEventTypeEnumStateMachine.sendEvent(VoteEventTypeEnum.开始活动);
                Thread.sleep(1000);
                voteEventTypeEnumStateMachine.sendEvent(VoteEventTypeEnum.创建活动);
                Thread.sleep(1000);
                voteEventTypeEnumStateMachine.sendEvent(VoteEventTypeEnum.结束活动);
                Thread.sleep(1000);
            }catch (Exception e){
                log.warn(e.getMessage());
            }
        });
        thread.start();
        return Result.success();
    }

}
