package com.spark.config;

import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/29 22:01
 * @Description
 */
@Component
@WithStateMachine(name = "OrderStateMachine")
public class OrderStatusListener {

    @OnTransition(source = "CREATED", target = "PAID")
    public void payTransition() {
        System.out.println("支付成功，状态从 CREATED 变更为 PAID");
    }

    @OnTransition(source = "PAID", target = "SHIPPED")
    public void deliverTransition() {
        System.out.println("发货成功，状态从 PAID 变更为 SHIPPED");
    }

    @OnTransition(source = "SHIPPED", target = "COMPLETED")
    public void receiveTransition() {
        System.out.println("收货成功，状态从 SHIPPED 变更为 COMPLETED");
    }
}
