package com.spark.properties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class SecurityProperties {
    /**
     * accessToken 过期时间(单位：秒)，默认1小时
     */
    private int accessTokenExpire = 60 * 60 * 12;
    /**
     * refreshToken 过期时间(单位：秒)，默认14天
     */
    private int refreshTokenExpire = 60 * 60 * 24 * 14;
}
