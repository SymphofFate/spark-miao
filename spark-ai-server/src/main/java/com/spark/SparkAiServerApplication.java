package com.spark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.spark.repository")
public class SparkAiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkAiServerApplication.class, args);
    }

}
