package com.spark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spark.dao.OssDao;
import com.spark.dto.OssDto;
import com.spark.entity.OssEntity;
import com.spark.enums.FileTypeEnum;
import com.spark.service.OssService;
import com.spark.utils.JSONUtils;
import com.spark.utils.Result;
import com.spark.utils.PageData;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/22 17:12
 * @Description
 */
@Service
public class OssServiceImpl extends ServiceImpl<OssDao, OssEntity> implements OssService {

    @Resource
    private OssDao dao;

    @Override
    public Result upload(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String property = System.getProperty("user.dir");
        File file1 = new File(property + "/oss/" + file.getOriginalFilename());
        file.transferTo(file1);
        OssEntity entity = OssEntity.builder().fileName(name).url("http://localhost:8080/oss/").build();
        save(entity);
        return Result.success();
    }

    @Override
    public void download(String path, HttpServletResponse response) throws FileNotFoundException {
        String property = System.getProperty("user.dir");
        File file = new File(property + "/oss/" + path);
        FileInputStream fileInputStream = new FileInputStream(property + "/oss/" + path);
        // 设置输出的格式
        response.reset();
        response.setContentType("application/octet-stream");
        // 循环取出流中的数据
        byte[] b = new byte[1000];
        int len;
        try {
            while ((len = fileInputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preview(String path, HttpServletResponse response) throws FileNotFoundException {
        String property = System.getProperty("user.dir");
        File file = new File(property + "/oss/" + path);
        FileInputStream fileInputStream = new FileInputStream(property + "/oss/" + path);
        // 设置输出的格式
        response.reset();
        String lowerCase = path.substring(path.lastIndexOf(".")).toLowerCase().replace(".","");
        response.setContentType(FileTypeEnum.getContentType(lowerCase));
        response.setCharacterEncoding("UTF-8");
        // 循环取出流中的数据
        byte[] b = new byte[1000];
        int len;
        try {
            while ((len = fileInputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueryWrapper<OssEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<OssEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    @Override
    public Result page(Map<String, Object> params) {
        QueryWrapper<OssEntity> wrapper = getWrapper(params);
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        PageDTO<OssEntity> pageDTO = dao.selectPage(new PageDTO<>(currentPage, pageSize), wrapper);
        List<OssDto> dtos = JSONUtils.listParse(pageDTO.getRecords(), OssDto.class);
        return Result.success(new PageData<>(dtos,pageDTO.getTotal()));
    }
}
