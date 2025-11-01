package com.spark.service.impl;

import com.spark.service.McpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/29 15:14
 * @Description
 */
@Slf4j
@Service
public class McpServiceImpl implements McpService {
    @Override
    @Tool(description = "通过城市名称获取城市温度")
    public String getWeatherByCity(@ToolParam(description = "城市名称") String cityName) {
        log.info("mcp被调用");
        return cityName + "今天的温度是" + (new java.util.Random().nextInt(9) + 1) * 6;
    }

    @Tool(description = "获取当前时间")
    public String getDate(){
        log.info("调用时间工具");
        return new Date().toLocaleString();
    }
}
