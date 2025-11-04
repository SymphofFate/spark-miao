package com.spark.controller;

import com.spark.service.OssService;
import com.spark.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/22 17:04
 * @Description
 */
@Tag(name = "文件服务")
@RestController
@RequestMapping("oss")
@CrossOrigin
public class OssController {

    @Resource
    private OssService service;

    @Operation(summary = "文件上传")
    @PostMapping("upload")
    public Result upload(MultipartFile file) throws IOException {
        return service.upload(file);
    }

    @Operation(summary = "文件下载")
    @GetMapping("download/{path}")
    public void download(@PathVariable("path") String path, HttpServletResponse response) throws FileNotFoundException {
        service.download(path,response);
    }

    @Operation(summary = "文件预览")
    @GetMapping("preview/{path}")
    public void preview(@PathVariable("path") String path, HttpServletResponse respons) throws FileNotFoundException {
        service.preview(path,respons);
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




}
