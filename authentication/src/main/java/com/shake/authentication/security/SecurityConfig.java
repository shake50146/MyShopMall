package com.shake.authentication.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author: SecurityConfig
 * @Description Spring Security配置類
 * @Date 2023/10/17
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityUserDetailService securityUserDetailService;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;


    /**
     * spring security 自帶的加密方法
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Swagger URL
     */
    private static final String[] excludedSwaggerAuthPages = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/webjars/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/swagger-ui.html/**",
            "/swagger-ui/**"
    };

    /**
     * auth URL
     */
    private static final String[] excludedAuthPages = {
            "/auth/login/**",
            "/auth/register/**",
            "/security/**"
    };

    /**
     * 自訂義配置Security filter chain和訪問權限/登入頁面配置
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

                    //csrf 跨域請求偽造 使用前後端分離需要禁用
        return http.csrf(csrf -> csrf.disable())
                //設置請求的授權規則
                .authorizeHttpRequests(
                        authorize -> authorize
                                //允許 register含以下所有頁面的請求
                                .requestMatchers(excludedAuthPages).permitAll()
                                //允許Swagger Api的頁面
                                .requestMatchers(excludedSwaggerAuthPages).permitAll()
                                .anyRequest().authenticated()
                )
                //前後端分離是無狀態的, 不需要session, 直接禁用
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //處理身分認證
                .authenticationManager(authenticationManager())
                //添加自訂義的Filter進filter chain
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    /**
     * 設置授權檢查管理的bean
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
        // 若有多個驗證方式，可使用：
        // List.of(daoAuthenticationProvider(), customAuthenticationProvider())
    }


    /**
     * 設置獲取UserDetail信息的Bean
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider
                = new DaoAuthenticationProvider();
        //DaoAuthenticationProvider 從自訂義的userDetailServiceImpl.loadUserByUsername方法獲取UserDetails
        authenticationProvider.setUserDetailsService(userDetailsService());
        //設置密碼編輯器
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    //獲取用戶
    @Bean
    public UserDetailsService userDetailsService() {
        // 調用 JwtUserDetailService實例執行實際校驗
        return username -> securityUserDetailService.loadUserByUsername(username);
    }

    /**
     * 使用Swagger API ,設置Authorization Header key-value值用
     * @return
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
