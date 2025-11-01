package com.spark.exception;

import cn.hutool.json.JSONUtil;
import com.spark.utils.Result;
import com.spark.enums.RequestCodeTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader(  "Access-Control-Allow-Origin","*");
        response.getWriter().print(JSONUtil.parse(Result.failure(RequestCodeTypeEnum.NOT_LOGGED_IN)));
    }
}
