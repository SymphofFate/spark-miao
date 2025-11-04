package com.spark.filiter;

import cn.hutool.core.util.StrUtil;
import com.spark.cache.TokenStoreCache;
import com.spark.entity.User;
import com.spark.entity.UserDetail;
import com.spark.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    @Resource
    private TokenStoreCache tokenStoreCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = TokenUtils.getAccesstoken(request);
        // accessToken为空，表示未登录
        if (StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (tokenStoreCache==null){
            logger.info("tokenStoreCache没找到");
        }
        User user = tokenStoreCache.getUser(token);
        //token过期或者token异常
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (tokenStoreCache.getKeyExpire(token)<60){
            tokenStoreCache.expireKey(token,2);
        }
        UserDetail userDetail = new UserDetail();
        userDetail.setUser(user);
        // 用户存在
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, userDetail.getAuthorities());
        // 新建 SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        //将用户信息保存到上下文中
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}
