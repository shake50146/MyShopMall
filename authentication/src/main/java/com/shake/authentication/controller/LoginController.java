package com.shake.authentication.controller;

import com.shake.authentication.feign.AccountFeignService;
import com.shake.authentication.model.entity.LoginEntity;
import com.shake.authentication.model.entity.RegisterEntity;
import com.shake.authentication.model.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author: LoginController
 * @Description 此類的所有方法都有調用OpenFeign, 所以在類上加上熔斷, 就不用在所有類上加熔斷
 * @Date 2023/11/8
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AccountFeignService accountFeignService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterEntity request){
        //遠端調用account service
        ResponseEntity<UserEntity> register = accountFeignService.register(request);
        return register;
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestBody LoginEntity loginEntity){
        //遠端調用account service
        ResponseEntity<UserEntity> login = accountFeignService.login(loginEntity);
        return login;
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){

        //獲取請求頭中的用戶名
        String username = request.getHeader("Username");
        //前端token刪除需要再前端代碼設定

        //刪除SecurityContext的token
        SecurityContextHolder.clearContext();
        //刪除資料庫的token
        ResponseEntity<String> logout = accountFeignService.logout(username);
        return logout;
    }

    @GetMapping("/refresh")
    public ResponseEntity<UserEntity> refreshToken(HttpServletRequest request){

        //獲取請求頭中的用戶名
        String username = request.getHeader("Username");
        ResponseEntity<UserEntity> refresh = accountFeignService.refreshToken(username);
        return refresh;
    }
}
