package com.spark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spark.entity.OssEntity;
import com.spark.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/22 17:12
 * @Description
 */
public interface OssService extends IService<OssEntity> {
    Result upload(MultipartFile file) throws IOException;

    void download(String path, HttpServletResponse response) throws FileNotFoundException;

    void preview(String path, HttpServletResponse response) throws FileNotFoundException;

    Result page(Map<String, Object> params);
}
