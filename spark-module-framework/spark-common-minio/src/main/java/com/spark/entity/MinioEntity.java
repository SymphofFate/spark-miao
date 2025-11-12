package com.spark.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version 1.0
 * @Author Xing
 * @Date 2024/4/11 17:09
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioEntity {
    /**存储路径*/
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    //Not registered via @EnableConfigurationProperties, marked as Spring component, or scanned via @ConfigurationPropertiesScan
}
