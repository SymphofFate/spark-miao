package com.spark.security;

import com.spark.constant.ResourceConstant;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/28 11:14
 * @Description
 */
@Component
public class BasePassResource implements ResourceConstant {
    /**
     * 不进行认证的URL
     */
    public static final String [] IGNORING_URLS = {
            "/actuator/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/swagger/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/doc.html",
            "/auth/**",
            "/pay/**",
            "/test/aa",
            "/chat/**"
    };

    @Override
    public String[] getConstantList() {
        return IGNORING_URLS;
    }
}
