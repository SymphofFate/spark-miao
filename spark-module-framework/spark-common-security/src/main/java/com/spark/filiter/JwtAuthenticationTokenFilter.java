package com.spark.filiter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/27 21:34
 * @Description
 */
@Component
@Log4j2
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("访问");
//        if (request.getServletPath().equals("/test/bb")){
//            filterChain.doFilter(request,response);
//        }
//        log.info("鉴权");
//        //保存权限信息
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authenticationToken);
//        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request,response);
    }
}
