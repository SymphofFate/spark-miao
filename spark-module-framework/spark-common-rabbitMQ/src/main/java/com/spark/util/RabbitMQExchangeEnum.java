package com.spark.util;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/9/10 22:07
 * @Description
 */
public enum RabbitMQExchangeEnum {

    TEST_EXCHANGE("test_exchange"),
    DELAYED_EXCHANGE("delayed_exchange")
    ;

    private final String exchangeName;

    RabbitMQExchangeEnum(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeName(){
        return exchangeName;
    }
}
