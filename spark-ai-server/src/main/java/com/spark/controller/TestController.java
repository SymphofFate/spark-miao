package com.spark.controller;

import com.spark.entity.KnowledgeDocument;
import com.spark.service.TestService;
import com.spark.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/30 17:38
 * @Description
 */
@Slf4j
@Tag(name = "测试服务")
@RestController
@RequestMapping("test")
@CrossOrigin
public class TestController {
    @Resource
    private TestService service;

    @GetMapping("know")
    @Operation(summary = "获取知识库数据")
    public Result list(){
        return Result.success(service.getList());
    }

    @PostMapping("save")
    @Operation(summary = "向知识库添加数据")
    public Result save(@RequestBody KnowledgeDocument document){
        service.saveNewKnow(document);
        return Result.success();
    }



}
