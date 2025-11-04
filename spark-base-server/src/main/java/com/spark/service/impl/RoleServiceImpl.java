package com.spark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spark.dao.RoleDao;
import com.spark.dto.RoleDto;
import com.spark.entity.RoleEntity;
import com.spark.service.RoleService;
import com.spark.utils.JSONUtils;
import com.spark.utils.Result;
import com.spark.utils.PageData;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 19:12
 * @Description
 */
@Service
@Log4j2
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {
    @Resource
    private RoleDao dao;

    public QueryWrapper<RoleEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    @Override
    public Result page(Map<String, Object> params) {
        QueryWrapper<RoleEntity> wrapper = getWrapper(params);
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        PageDTO<RoleEntity> roleEntityPageDTO = dao.selectPage(new PageDTO<>(currentPage, pageSize), wrapper);
        List<RoleDto> dtos = JSONUtils.listParse(roleEntityPageDTO.getRecords(), RoleDto.class);
        return Result.success(new PageData<>(dtos, roleEntityPageDTO.getTotal()));
    }
}
