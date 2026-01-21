package com.spark.service.impl;

import com.spark.dto.VoteDto;
import com.spark.enums.RequestCodeTypeEnum;
import com.spark.service.TestService;
import com.spark.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/29 14:24
 * @Description
 */
@Log4j2
@Service
public class TestServiceImpl implements TestService {


    @Resource(name = "OrderStateMachine")
    private StateMachine<OrderState, OrderEvent> orderStateMachine;

    @Resource(name = "VoteStateMachine")
    private StateMachine<VoteStateTypeEnum, VoteEventTypeEnum> voteEventTypeEnumStateMachine;

    @Resource(name = "VoteStateMachineMemPersister")
     private StateMachinePersister<VoteStateTypeEnum,VoteEventTypeEnum,String> persist;

    @Resource(name = "stateMachineRedisPersister")
    private RedisStateMachinePersister persister;


    @Override
    public void statemachineTest() {
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
    }

    @Override
    public void voteStateMachineTest() {
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
    }

    @Override
    public Result voteTest(Integer code) {
        VoteDto build = VoteDto.builder().id(0L).name("流程测试").build();
        boolean sendEvent = sendEvent(VoteEventTypeEnum.getEnum(code), build);
        if (sendEvent){
            return Result.success();
        }
        return Result.failure(RequestCodeTypeEnum.FAILURE);
    }



    private synchronized boolean sendEvent(VoteEventTypeEnum eventTypeEnum, VoteDto dto){
        boolean result = false;
        try {
            voteEventTypeEnumStateMachine.start();
            persist.restore(voteEventTypeEnumStateMachine,dto.getId().toString());
            Message<VoteEventTypeEnum> vote = MessageBuilder.withPayload(eventTypeEnum).setHeader("vote", dto).build();
            result = voteEventTypeEnumStateMachine.sendEvent(vote);
            persist.persist(voteEventTypeEnumStateMachine,dto.getId().toString());
        }catch (Exception e){
            log.warn(e.getMessage());
        }finally {
            voteEventTypeEnumStateMachine.stop();
        }
        return result;
    }


}
