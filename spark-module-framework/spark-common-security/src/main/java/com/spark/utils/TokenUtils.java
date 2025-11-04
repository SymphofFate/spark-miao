package com.spark.utils;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TokenUtils {
    public static String generator(){
        return UUID.fastUUID().toString(true);
    }
    public static String getAccesstoken(HttpServletRequest request){
        String authorization = StrUtil.isBlank(request.getHeader("Authorization"))?"":request.getHeader("Authorization");
        log.info("Token: {}",authorization);
        if (authorization.contains("Bearer ")){
            authorization = authorization.replace("Bearer ","");
        }
        if (StrUtil.isBlank(authorization)){
            authorization = request.getParameter("token");
        }
        return authorization;
    }
}
