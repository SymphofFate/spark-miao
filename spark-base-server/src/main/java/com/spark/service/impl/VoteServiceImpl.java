package com.spark.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spark.dao.VoteDao;
import com.spark.dto.OssDto;
import com.spark.dto.TestDto;
import com.spark.dto.VoteDto;
import com.spark.entity.VoteEntity;
import com.spark.entity.VoteEntity;
import com.spark.enums.RequestCodeTypeEnum;
import com.spark.sendList.TestSend;
import com.spark.service.VoteService;
import com.spark.util.RabbitMQQueueConstant;
import com.spark.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/2 10:58
 * @Description
 */
@Service
@Log4j2
public class VoteServiceImpl extends ServiceImpl<VoteDao, VoteEntity> implements VoteService {
    @Resource
    private VoteDao dao;

    @Resource
    private TestSend send;

    @Resource(name = "VoteStateMachine")
    private StateMachine<VoteStateTypeEnum, VoteEventTypeEnum> voteEventTypeEnumStateMachine;

    @Resource(name = "VoteStateMachineMemPersister")
    private StateMachinePersister<VoteStateTypeEnum,VoteEventTypeEnum,String> persist;

    public QueryWrapper<VoteEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<VoteEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    @Override
    public Result page(Map<String, Object> params) {
        QueryWrapper<VoteEntity> wrapper = getWrapper(params);
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        PageDTO<VoteEntity> pageDTO = dao.selectPage(new PageDTO<>(currentPage, pageSize), wrapper);
        List<OssDto> dtos = JSONUtils.listParse(pageDTO.getRecords(), OssDto.class);
        return Result.success(new PageData<>(dtos,pageDTO.getTotal()));
    }

    @Override
    public Result info(Long id) {
        VoteEntity byId = getById(id);
        VoteDto build = VoteDto.builder().build();
        BeanUtil.copyProperties(byId,build,"");
        return Result.success(build);
    }


    //TODO：将投票行为异步化，发送到队列里处理，
    /// 问题1：异步任务如何减少等待时间，不能等待所有人都投完再去处理！而且还要确认每个人的投票状态
    /// 问题2：如何保证提交幂等性，就是每个人都只能提交一次（暂时考虑使用状态机处理：未投票--->已投票）
    @Override
    public Result vote() {
        try {
            for (int i = 0; i < 100; i++) {
                send.testQueueSend(TestDto.builder().test("测试").build());
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.failure(RequestCodeTypeEnum.FAILURE);
        }

        return Result.success();
    }


    @Override
    public Result create(VoteDto dto) {
        VoteEntity build = VoteEntity.builder()
                .info(dto.getInfo())
                .name(dto.getName())
                .type(dto.getType())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .nature(dto.getNature())
                .build();
        boolean save = save(build);
        sendEvent(VoteEventTypeEnum.创建活动,VoteDto.builder().id(build.getId()).build());
        if (save){
            return Result.success();
        };
        return Result.failure(RequestCodeTypeEnum.FAILURE);
    }

    @Override
    public Result start(Long id) {
        if (ObjectUtil.isNull(getById(id))){
            return Result.failure(RequestCodeTypeEnum.FAILURE);
        }
        UpdateWrapper<VoteEntity> wrapper = new UpdateWrapper<>();
        wrapper.set("status",3);
        wrapper.set("start_time",new Date());
        wrapper.eq("id",id);
        boolean sendEvent = sendEvent(VoteEventTypeEnum.开始活动, VoteDto.builder().id(id).build());
        if (sendEvent){
            dao.update(wrapper);
            return Result.success();
        }
        return Result.failure(RequestCodeTypeEnum.FAILURE);
    }

    @Override
    public Result stop(Long id) {
        if (ObjectUtil.isNull(getById(id))){
            return Result.failure(RequestCodeTypeEnum.FAILURE);
        }
        UpdateWrapper<VoteEntity> wrapper = new UpdateWrapper<>();
        wrapper.set("status",2);
        wrapper.eq("id",id);
        boolean sendEvent = sendEvent(VoteEventTypeEnum.暂停活动, VoteDto.builder().id(id).build());
        if (sendEvent){
            dao.update(wrapper);
            return Result.success();
        }
        return Result.failure(RequestCodeTypeEnum.FAILURE);
    }

    @Override
    public Result end(Long id) {
        if (ObjectUtil.isNull(getById(id))){
            return Result.failure(RequestCodeTypeEnum.FAILURE);
        }
        UpdateWrapper<VoteEntity> wrapper = new UpdateWrapper<>();
        wrapper.set("status",1);
        wrapper.set("end_time",new Date());
        wrapper.eq("id",id);
        boolean sendEvent = sendEvent(VoteEventTypeEnum.结束活动, VoteDto.builder().id(id).build());
        if (sendEvent){
            dao.update(wrapper);
            return Result.success();
        }
        return Result.failure(RequestCodeTypeEnum.FAILURE);
    }


    private synchronized boolean sendEvent(VoteEventTypeEnum eventTypeEnum, VoteDto dto){
        boolean result = false;
        try {
            voteEventTypeEnumStateMachine.start();
            persist.restore(voteEventTypeEnumStateMachine,dto.getId().toString());
            Message<VoteEventTypeEnum> vote = MessageBuilder.withPayload(eventTypeEnum).setHeader("vote", dto).build();
            result = voteEventTypeEnumStateMachine.sendEvent(vote);
            persist.persist(voteEventTypeEnumStateMachine,dto.getId().toString());
        }catch (Exception e){
            log.warn(e.getMessage());
        }finally {
            voteEventTypeEnumStateMachine.stop();
        }
        return result;
    }

    @RabbitListener(queues = {RabbitMQQueueConstant.TEST_QUEUE},concurrency = "1-2")
    public void test(TestDto dto) throws InterruptedException {
//        Thread.sleep(4000);
        log.info("成功消费：{}",dto);
    }




}
