package com.spark.cache;

import com.spark.entity.User;
import com.spark.entity.UserDetail;

public interface TokenStoreCache {

    /**
     * 根据token获取信息
     *
     * @param accessToken
     * @return
     */
    public User getUser(String accessToken);

    /**
     * 持久保存
     *
     * @param accessToken
     * @param user
     */
    public void saveUser(String accessToken, UserDetail user);

    /**
     * 保存token
     *
     * @param accessToken
     * @param user        保存信息
     * @param expire      保存时长，单位
     */
    public void saveUser(String accessToken, UserDetail user, long expire);


    /**
     * 延长token时间
     *
     * @param accessToken token
     * @param expire      延长时间
     */
    public void expireKey(String accessToken, long expire);

    //    public
    public Long getKeyExpire(String accessToken);
}
