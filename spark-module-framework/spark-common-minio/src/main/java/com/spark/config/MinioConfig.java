package com.spark.config;

import com.spark.entity.MinioEntity;
import io.minio.MinioClient;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/6 12:40
 * @Description
 */
@Configuration
public class MinioConfig {
    @Resource
    private MinioEntity entity;

    @Bean
    @SneakyThrows
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(entity.getEndpoint())
                .credentials(entity.getAccessKey(),entity.getSecretKey())
                .build();
    }
}
