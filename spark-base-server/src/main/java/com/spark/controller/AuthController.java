package com.spark.controller;

import com.spark.dto.LoginDto;
import com.spark.service.UserService;
import com.spark.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "登陆服务")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @Operation(summary = "登陆")
    @PostMapping("/login")
    public Result Login(@RequestBody LoginDto dto){
        return userService.Login(dto);
    }


    @Operation(summary = "注册")
    @PostMapping("register")
    public Result Register(@RequestBody LoginDto dto){
        return userService.Register(dto);
    }

    @GetMapping("info")
    @Operation(summary = "获取用户信息")
    public Result info(){
        return userService.info();
    }
}
