package com.shake.authentication.security;


import com.shake.authentication.feign.AccountFeignService;
import com.shake.authentication.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author: TokenAuthenticationFilter
 * @Description 自訂義 token解析過濾器
 * @Date 2023/10/17
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountFeignService accountFeignService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().startsWith("/auth/login")
                || request.getRequestURI().startsWith("/auth/register")
                || request.getRequestURI().startsWith("/security/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        // 從請求頭中獲取token
        String token = request.getHeader("Authorization");
        if(token != null){
            Claims claims = jwtUtil.parse(token);
            String username = claims.getSubject();
            //遠程調用 用戶服務的getToken 獲取資料庫存儲的token來進行比對
            String saveToken = accountFeignService.getToken(username).getBody();
            //確認token時效跟與數據庫內存儲的token比對
            if (jwtUtil.validation(token) && token.equals(saveToken)) {
                //將認證對象放到Security Context上下文中 讓後續的請求處理能夠調用
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                throw new JwtException("Invalid or Expired JWT token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
