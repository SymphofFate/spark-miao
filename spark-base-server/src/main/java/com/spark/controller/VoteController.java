package com.spark.controller;

import com.spark.dto.VoteDto;
import com.spark.entity.VoteEntity;
import com.spark.enums.RequestCodeTypeEnum;
import com.spark.service.VoteService;
import com.spark.utils.Result;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/12/2 11:00
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/vote")
@Tag(name = "活动服务")
public class VoteController {

    @Resource
    private VoteService service;

    @PostMapping("create")
    @Operation(summary = "创建活动实体")
    public Result createVote(@RequestBody VoteDto dto){
        return service.create(dto);
    }

    @GetMapping("start")
    @Operation(summary = "开始活动")
    public Result start(@Param("id")Long id){
        return service.start(id);
    }

    @GetMapping("stop")
    @Operation(summary = "暂停活动")
    public Result stop(@Param("id")Long id){
        return service.stop(id);
    }

    @GetMapping("end")
    @Operation(summary = "结束活动")
    public Result end(@Param("id")Long id){
        return service.end(id);
    }


    @Operation(summary = "分页查找")
    @GetMapping("page")
    @Parameters({
            @Parameter(name = "pageSize", description = "分页限制", required = true),
            @Parameter(name = "currentPage", description = "页码", required = true)
    })
    public Result page(@RequestParam(required = false) Map<String, Object> params) {
        return service.page(params);
    }


    @GetMapping("info")
    @Operation(summary = "获取活动信息")
    public Result getInfo(@Param("id")Long id){
        return service.info(id);
    }


}
