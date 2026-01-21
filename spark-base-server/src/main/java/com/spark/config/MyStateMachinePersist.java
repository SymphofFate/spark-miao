package com.spark.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.spark.entity.StateMachinePersistEntity;
import com.spark.utils.JSONUtils;
import com.spark.utils.RedisUtils;
import com.spark.utils.VoteEventTypeEnum;
import com.spark.utils.VoteStateTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/2 14:35
 * @Description
 */
@Log4j2
@Component
public class MyStateMachinePersist implements StateMachinePersist<VoteStateTypeEnum, VoteEventTypeEnum,String> {

    @Resource
    private RedisUtils template;

    private Map map = new HashMap();

    @Override
    public void write(StateMachineContext<VoteStateTypeEnum, VoteEventTypeEnum> context, String object) throws Exception {
        log.info("持久化状态机,context:{},contextObj:{}", context, object);
        StateMachinePersistEntity build = StateMachinePersistEntity.builder()
                .id(context.getId())
                .state(context.getState().getCode())
                .event(context.getEvent()==null?null:context.getEvent().getCode())
                .eventHead(null)
                .build();
        template.set("stateMachine:"+object,build);
    }

    @Override
    public StateMachineContext<VoteStateTypeEnum, VoteEventTypeEnum> read(String object) throws Exception {
        log.info("获取状态机,contextObj:{}",object);
        log.info("获取：{}",template.get("stateMachine:" + object));
        StateMachinePersistEntity entity = JSONUtils.objParse(template.get("stateMachine:" + object), StateMachinePersistEntity.class);
        if (ObjectUtil.isNull(entity)){
            return new DefaultStateMachineContext<>(null,null,null,null);
        }
        DefaultStateMachineContext<VoteStateTypeEnum, VoteEventTypeEnum> context =
                new DefaultStateMachineContext<>(VoteStateTypeEnum.getEnum(entity.getState()),
                        VoteEventTypeEnum.getEnum(entity.getEvent()), null, null);
        return context;
    }
}
