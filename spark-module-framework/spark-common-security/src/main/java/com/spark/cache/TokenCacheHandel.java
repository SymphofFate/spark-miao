package com.spark.cache;

import com.spark.cache.TokenStoreCache;
import com.spark.entity.User;
import com.spark.entity.UserDetail;
import com.spark.properties.SecurityProperties;
import com.spark.utils.JSONUtils;
import com.spark.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TokenCacheHandel implements TokenStoreCache {

    @Resource
    private RedisUtils template;

    @Resource
    private SecurityProperties properties;

    @Override
    public User getUser(String accessToken) {
        log.info(accessToken);
        Object object = template.get(tokenHandle(accessToken));
        return JSONUtils.objParse(object, User.class);
    }

    @Override
    public void saveUser(String accessToken, UserDetail user) {
        template.set(tokenHandle(accessToken), user.getUser());
    }

    @Override
    public void saveUser(String accessToken, UserDetail user, long expire) {
        template.set(tokenHandle(accessToken), user.getUser(), expire);
    }

    @Override
    public void expireKey(String accessToken, long expire) {
        template.expire(tokenHandle(accessToken), expire, TimeUnit.HOURS);
    }

    @Override
    public Long getKeyExpire(String accessToken) {
        return template.getExpire(tokenHandle(accessToken));
    }

    public void saveUserDefault(String accessToken, UserDetail user) {
        saveUser(accessToken, user, properties.getAccessTokenExpire());
    }

    private String tokenHandle(String token) {
        return "token:" + token;
    }
}
