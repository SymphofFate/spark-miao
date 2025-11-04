package com.spark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spark.cache.TokenCacheHandel;
import com.spark.dao.UserEntityDao;
import com.spark.dto.LoginDto;
import com.spark.dto.UserDto;
import com.spark.entity.UserEntity;
import com.spark.enums.RequestCodeTypeEnum;
import com.spark.entity.User;
import com.spark.entity.UserDetail;
import com.spark.service.UserService;
import com.spark.utils.JSONUtils;
import com.spark.utils.Result;
import com.spark.utils.PageData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class UserServiceImpl extends ServiceImpl<UserEntityDao, UserEntity> implements UserService {

    /**
     * 编程式事务
     */
    @Autowired
    private TransactionTemplate template;

    @Autowired
    private UserEntityDao dao;

    @Autowired
    private TokenCacheHandel handel;






    public QueryWrapper<UserEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    @Override
    public User GetUserByName(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        UserEntity userEntity = dao.selectOne(wrapper);
        return JSONUtils.objParse(userEntity, User.class);
    }

    @Override
    public Result Login(LoginDto dto) {
        User user = GetUserByName(dto.getUsername());
        if (user == null) {
            return Result.failure(RequestCodeTypeEnum.NOT_LOGGED_IN);
        }
        if (verifyPassword(dto.getPassword(), user.getPassword())) {
            String token = UUID.randomUUID().toString().replace("-", "");
            handel.saveUserDefault(token, new UserDetail(user));
            return Result.success(token);
        }
        return Result.failure(403, "账号或密码错误");
    }

    @Override
    public Result Register(LoginDto dto) {
        User byName = GetUserByName(dto.getUsername());
        if (byName != null) {
            return Result.failure(502, "账号已存在");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(dto.getUsername());
        userEntity.setEmail(dto.getEmail());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        userEntity.setCreator(0L);
        userEntity.setCreateDate(new Date());
        template.execute(action -> {
            try {
                dao.insert(userEntity);
            } catch (Exception e) {
                action.setRollbackOnly();
                log.error("注册事务异常");
                return Result.failure(501,"注册事务异常");
            }
            String token = UUID.randomUUID().toString().replace(" ", "");
            User user = GetUserByName(dto.getUsername());
            handel.saveUserDefault(token, new UserDetail(user));
            return Result.success(token);
        });
        return Result.failure(RequestCodeTypeEnum.FAILURE);
    }

    @Override
    public Result Page(Map<String, Object> params) {
        QueryWrapper<UserEntity> wrapper = getWrapper(params);
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        PageDTO<UserEntity> userEntityPageDTO = dao.selectPage(new PageDTO<>(currentPage, pageSize), wrapper);
        List<UserDto> dtos = JSONUtils.listParse(userEntityPageDTO.getRecords(), UserDto.class);
        return Result.success(new PageData<>(dtos, userEntityPageDTO.getTotal()));
    }

    @Override
    public Result info() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(principal);
    }

    @Override
    public Result save(UserDto dto) {
        UserEntity userEntity = new UserEntity();
        log.info(userEntity);
        BeanUtils.copyProperties(dto, userEntity);
        save(userEntity);
        return Result.success();
    }

    /**
     * 对比密码是否正确
     *
     * @param oldPass 数据库保存密码
     * @param newPass 用户输入密码
     * @return 比对结果
     */
    private boolean verifyPassword(String oldPass, String newPass) {
        return new BCryptPasswordEncoder().matches(oldPass, newPass);
    }
}
