package com.shake.gateway.security;

import com.shake.gateway.entity.UserEntity;
import com.shake.gateway.feign.AccountFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author: UserDetailServiceImpl
 * @Description 用戶信息的實現類 : 可以返回自訂義的用戶對象以用於加載用戶信息
 * @Date 2023/10/17
 */
@Component
public class SecurityUserDetailService implements ReactiveUserDetailsService {

    @Autowired
    private AccountFeignService accountFeignService;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        UserEntity user = accountFeignService.getUser(username).getBody();
        if(user == null){
            throw new UsernameNotFoundException("Username Not Found");
        }
        return null;
    }
}
