package com.spark.controller;

import com.spark.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    

    @GetMapping("/hello")
    public Result hello() {
        return Result.success("Hello, Spark!");
    }

    @GetMapping("aa")
    public Result test(){
        return Result.success();
    }

    @GetMapping("bb")
    public Result testBb(){
        return Result.success();
    }

}
