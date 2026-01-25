package com.spark.sendList;

import cn.hutool.json.JSONUtil;
import com.spark.util.RabbitMQExchangeEnum;
import com.spark.util.RabbitMQQueueConstant;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/9/10 22:17
 * @Description
 */
@Component
public class TestSend {
    private static final Logger log = LoggerFactory.getLogger(TestSend.class);
    @Resource
    private RabbitTemplate template;


    /// 测试队列
    public void testQueueSend(Object object){
        retrySend(RabbitMQQueueConstant.TEST_QUEUE,object,0,0);
    }

    /**
     * 使用默认交换机
     * @param queueName
     * @param object 只能使用对象
     * @param retryTime
     * @param maxRetryTime
     */
    public void retrySend(String queueName,Object object,Integer retryTime,Integer maxRetryTime){
        if (Objects.isNull(maxRetryTime)){
            maxRetryTime = 3;
        }
        if (retryTime<=maxRetryTime){
            try {
                template.convertAndSend(queueName,JSONUtil.parse(object));
            }catch (AmqpException e){
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                retrySend(queueName,object,retryTime+1,maxRetryTime);
            }
        }else {
            log.error("队列发送失败，队列名：{}，消息体：{}",queueName, JSONUtil.parse(object));
        }
    }

    /**
     * 使用自定义交换机
     * @param exchangeName
     * @param queueName
     * @param object
     * @param retryTime
     * @param maxRetryTime
     */
    public void retrySend(String exchangeName,String queueName,Object object,Integer retryTime,Integer maxRetryTime){
        if (Objects.isNull(maxRetryTime)){
            maxRetryTime = 3;
        }
        if (retryTime<=maxRetryTime){
            try {
                template.convertAndSend(exchangeName,queueName,JSONUtil.parse(object));
            }catch (AmqpException e){
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                retrySend(exchangeName,queueName,object,retryTime+1,maxRetryTime);
            }
        }else {
            log.error("队列发送失败，队列名：{}，消息体：{}",queueName, JSONUtil.parse(object));
        }
    }


    public void delayedQueueSend(Object object) {
//        retrySend(RabbitMQExchangeEnum.DELAYED_EXCHANGE.getExchangeName(),RabbitMQQueueConstant.DELAYED_QUEUE,object,0,0);
        retrySend("",RabbitMQQueueConstant.DELAYED_QUEUE,object,0,0);
    }

}
