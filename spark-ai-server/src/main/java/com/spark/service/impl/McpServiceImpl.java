package com.spark.service.impl;

import com.spark.dto.Commodity;
import com.spark.service.McpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/29 15:14
 * @Description
 */
@Slf4j
@Service
public class McpServiceImpl implements McpService {

    double qian  = 1000.00;

    @Override
    @Tool(description = "通过城市名称获取城市温度")
    public String getWeatherByCity(@ToolParam(description = "城市名称") String cityName) {
        log.info("mcp被调用");
        return cityName + "今天的温度是" + (new java.util.Random().nextInt(9) + 1) * 6;
    }

    @Override
    @Tool(description = "商城商品列表")
    public List<Commodity> getCommodity(double money) {
        log.info("商城被调用");
        Commodity commodity = Commodity.builder().name("理智补剂").money(600.00).number(1).build();
        Commodity built = Commodity.builder().name("泡面").money(12.50).number(5).build();
        Commodity built2 = Commodity.builder().name("衣服").money(72.3).number(1).build();
        Commodity built3 = Commodity.builder().name("可乐").money(3.00).number(1).build();
        List<Commodity> list = new ArrayList<>();
        list.add(commodity);
        list.add(built);
        list.add(built2);
        list.add(built3);
        return list;
    }

    @Tool(description = "获取当前时间")
    public String getDate(){
        log.info("调用时间工具");
        return new Date().toLocaleString();
    }


    @Tool(description = "获取账号余额")
    public Double obtainAccountBalance(){
        log.info("获取余额");
        return  qian;
    }

    @Tool(description = "扣除账号余额，返回扣除后的账户余额")
    public Double eductccountalance(@ToolParam(description = "扣除的金额") double money){
        log.info("消费余额");
        qian -=money;
        return qian;
    }
}
