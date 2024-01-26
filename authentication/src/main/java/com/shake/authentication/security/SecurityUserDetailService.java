package com.shake.authentication.security;

import com.shake.authentication.feign.AccountFeignService;
import com.shake.authentication.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author: UserDetailServiceImpl
 * @Description 用戶信息的實現類 : 可以返回自訂義的用戶對象以用於加載用戶信息
 * @Date 2023/10/17
 */
@Component
public class SecurityUserDetailService implements UserDetailsService {

    @Autowired
    private AccountFeignService accountFeignService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = accountFeignService.getAuthUser(username).getBody();
        if(user == null){
            throw new UsernameNotFoundException("Username Not Found");
        }

        return user;
    }
}
