package com.spark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spark.dto.LoginDto;
import com.spark.dto.UserDto;
import com.spark.entity.UserEntity;
import com.spark.entity.User;
import com.spark.utils.Result;

import java.util.Map;

public interface UserService extends IService<UserEntity> {

    public User GetUserByName(String username);

    Result Login(LoginDto dto);

    Result Register(LoginDto dto);

    Result Page(Map<String, Object> params);

    Result info();

    Result save(UserDto dto);
}
