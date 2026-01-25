package com.spark.listener;

import com.spark.dto.TestDto;
import com.spark.service.VoteService;
import com.spark.util.RabbitMQQueueConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2026/1/25 18:07
 * @Description
 */
@Component
@Slf4j
public class VoteListener {

    @Resource
    private VoteService voteService;

    /*注意，消息队列一定要做异常抛出，否则队列消息无法消费会一直执行  ---错的*/
    /// 制作异常处理不抛出，因为是自动确认，会消费不掉
    @RabbitListener(queues = {RabbitMQQueueConstant.PROCESS_QUEUE},concurrency = "1-2")
    public void test(List<TestDto> dtos){
        try {
            voteService.Test01(dtos);
        }catch (Exception e){
            log.warn(e.getMessage());
        }

    }
}
