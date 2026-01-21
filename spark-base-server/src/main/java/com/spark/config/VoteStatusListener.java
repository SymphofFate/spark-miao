package com.spark.config;

import com.spark.utils.VoteStateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 20
 * 25/11/30 18:59
 * @Description
 */
@Slf4j
@Component
@WithStateMachine(name = "VoteStateMachine")
public class VoteStatusListener {

    @OnTransition(source = "未创建",target = "未开始")
    public void create(){
        log.info("创建活动");
    }


    @OnTransition(source = "未开始",target = "进行中")
    public void start(Message<VoteStateTypeEnum> message){
        log.info("开始活动:{}",message);
    }

    @OnTransition(source = "进行中",target = "暂停")
    public void pause(Message<VoteStateTypeEnum> message){
        log.info("活动暂停:{}",message);
    }

    @OnTransition(source = "暂停",target = "进行中")
    public void inProgress(){
        log.info("活动继续");
    }

    @OnTransition(target = "结束")
    public void over(Message<VoteStateTypeEnum> message){
        log.info("活动结束:{}",message);
    }

//    @OnTransition(source = "进行中",target = "结束")
//    public void over02(){
//        log.info("活动直接结束");
//    }
}
