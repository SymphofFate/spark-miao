package com.spark.config;

import com.spark.util.RabbitMQExchangeEnum;
import com.spark.util.RabbitMQQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/9/10 22:01
 * @Description
 */
@Configuration
@Slf4j
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter(){
        //使用json序列化发送消息
        return new Jackson2JsonMessageConverter();
    }

    //测试消息发送队列
    @Bean
    public Queue testQueue(){
        return new Queue(RabbitMQQueueConstant.TEST_QUEUE,true);
    }

    @Bean
    public DirectExchange testExchange(){
        return new DirectExchange(RabbitMQExchangeEnum.TEST_EXCHANGE.getExchangeName());
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(testQueue()).to(testExchange()).withQueueName();
    }

    //延时队列
    @Bean
    public Queue delayedQueue(){
        Map<String, Object> args = new HashMap<>(3);
        // 设置死信交换机
        args.put("x-dead-letter-exchange", RabbitMQExchangeEnum.DELAYED_EXCHANGE.getExchangeName());
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", RabbitMQQueueConstant.PROCESS_QUEUE);
        // 设置队列消息过期时间（单位：毫秒），15分钟
        args.put("x-message-ttl", 9000);
        log.info("创建延迟队列：{}，TTL={}ms", RabbitMQQueueConstant.DELAYED_QUEUE, 9000);
        return new Queue(RabbitMQQueueConstant.DELAYED_QUEUE,true,false,false,args);
    }

    //处理队列
    @Bean
    public Queue processQueue(){
        return new Queue(RabbitMQQueueConstant.PROCESS_QUEUE,true,false,false);
    }

    @Bean
    public DirectExchange delayedExchange(){
        log.info("创建死信交换机：{}", RabbitMQExchangeEnum.DELAYED_EXCHANGE.getExchangeName());
        return new DirectExchange(RabbitMQExchangeEnum.DELAYED_EXCHANGE.getExchangeName(),true,false);
    }

    @Bean
    public Binding delayedBinding(){
        return BindingBuilder.bind(processQueue()).to(delayedExchange())
                .with(RabbitMQQueueConstant.PROCESS_QUEUE);
    }

}
