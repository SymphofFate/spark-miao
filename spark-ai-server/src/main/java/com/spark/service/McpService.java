package com.spark.service;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/29 15:13
 * @Description
 */
public interface McpService {

    /**
     * 获取城市天气
     * @param cityName 城市名称
     * @return
     */
    public String getWeatherByCity(String cityName);

}
