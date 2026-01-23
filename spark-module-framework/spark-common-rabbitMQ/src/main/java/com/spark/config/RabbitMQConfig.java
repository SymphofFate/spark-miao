package com.spark.config;

import com.spark.util.RabbitMQExchangeEnum;
import com.spark.util.RabbitMQQueueConstant;
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
        args.put("x-dead-letter-exchange", RabbitMQExchangeEnum.DELAYED_EXCHANGE);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", RabbitMQQueueConstant.DELAYED_QUEUE);
        // 设置队列消息过期时间（单位：毫秒），15分钟
        args.put("x-message-ttl", 900000);
        return new Queue(RabbitMQQueueConstant.DELAYED_QUEUE,true,false,true,args);
    }

    @Bean
    public DirectExchange delayedExchange(){
        return new DirectExchange(RabbitMQExchangeEnum.DELAYED_EXCHANGE.getExchangeName());
    }

    @Bean
    public Binding delayedBinding(){
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).withQueueName();
    }
}
