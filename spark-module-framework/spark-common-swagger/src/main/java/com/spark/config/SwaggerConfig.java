package com.spark.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author Xing
 * @Date 2023/11/15 9:00
 */
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        Map<String, Object> map = new HashMap<>();
        contact.setName("喵了个咪");
        contact.setEmail("hist_xing@163.com");
        contact.setExtensions(map);
        contact.setUrl("https://gitee.com/a-tom-is-cry");

        License license = new License();
        license.setName("miao");
        return new OpenAPI().info(new Info()
                .title("MIAO框架")
                .description("一个基于springboot3.3.0的框架")
                .contact(contact)
                .version("1.0")
                .termsOfService("RESTful API"));
    }
}
