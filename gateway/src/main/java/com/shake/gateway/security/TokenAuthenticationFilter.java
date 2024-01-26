package com.shake.gateway.security;


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
        if (request.getServletPath().contains("/api/v1/auth") ||
                request.getServletPath().contains("/v3/api-docs") ||
                request.getServletPath().contains("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 從請求頭中獲取bearer開頭的token
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            //解析去掉Bearer前綴的 Token
            String frontToken = bearerToken.substring(7);

            Claims claims = jwtUtil.parse(frontToken);
            String username = claims.getSubject();
            //遠程調用 用戶服務的getToken 獲取資料庫存儲的token來進行比對
            String saveToken = accountFeignService.getToken(username).getBody();
            //確認token時效跟與數據庫內存儲的token比對
            if (jwtUtil.validation(frontToken) && frontToken.equals(saveToken)) {

                //將認證對象放到Security Context上下文中 讓後續的請求處理能夠調用,
                Authentication authentication = jwtUtil.getAuthentication(frontToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                throw new JwtException("Invalid or Expired JWT token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
