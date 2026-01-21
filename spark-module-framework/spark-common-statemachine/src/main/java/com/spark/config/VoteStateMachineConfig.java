package com.spark.config;

import com.spark.utils.JSONUtils;
import com.spark.utils.VoteEventTypeEnum;
import com.spark.utils.VoteStateTypeEnum;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/30 18:28
 * @Description
 */
@Configuration
@EnableStateMachine(name = "VoteStateMachine")
public class VoteStateMachineConfig extends StateMachineConfigurerAdapter<VoteStateTypeEnum, VoteEventTypeEnum> {

    @Resource
    private StateMachinePersist persist;

    @Override
    public void configure(StateMachineStateConfigurer<VoteStateTypeEnum, VoteEventTypeEnum> states) throws Exception {
        states.withStates()
                //初始状态
                .initial(VoteStateTypeEnum.未创建)
                .states(EnumSet.allOf(VoteStateTypeEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<VoteStateTypeEnum, VoteEventTypeEnum> transitions) throws Exception {
        transitions.withExternal()
                .source(VoteStateTypeEnum.未创建).target(VoteStateTypeEnum.未开始).event(VoteEventTypeEnum.  创建活动)
                .and()
                .withExternal()
                .source(VoteStateTypeEnum.未开始).target(VoteStateTypeEnum.进行中).event(VoteEventTypeEnum.开始活动)
                .and()
                .withExternal()
                .source(VoteStateTypeEnum.进行中).target(VoteStateTypeEnum.暂停).event(VoteEventTypeEnum.暂停活动)
                .and()
                .withExternal()
                .source(VoteStateTypeEnum.暂停).target(VoteStateTypeEnum.进行中).event(VoteEventTypeEnum.开始活动)
                .and()
                .withExternal()
                .source(VoteStateTypeEnum.暂停).target(VoteStateTypeEnum.结束).event(VoteEventTypeEnum.结束活动)
                .and()
                .withExternal()
                .source(VoteStateTypeEnum.进行中).target(VoteStateTypeEnum.结束).event(VoteEventTypeEnum.结束活动);
    }



    @Bean("VoteStateMachineMemPersister")
    public StateMachinePersister persister(){
        return new DefaultStateMachinePersister(persist);
    }


    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    /**
     * 持久化到redis中，在分布式系统中使用
     *
     * @return
     */
    @Bean(name = "stateMachineRedisPersister")
    public RedisStateMachinePersister<VoteEventTypeEnum, VoteStateTypeEnum> getRedisPersister() {
        RedisStateMachineContextRepository<VoteEventTypeEnum, VoteStateTypeEnum> repository = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist p = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(p);
    }

}
