package com.shake.gateway.controller;

import com.shake.authentication.feign.AccountFeignService;
import com.shake.gateway.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author: LoginController
 * @Description
 * @Date 2023/11/8
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AccountFeignService accountFeignService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterEntity request){

        ResponseEntity<UserEntity> register = accountFeignService.register(request);

        return register;

    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestBody LoginEntity loginEntity){

        ResponseEntity<UserEntity> login = accountFeignService.login(loginEntity);
        return login;
    }

    @GetMapping("/logout")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<Void> logout(){

        //獲取認證
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //從認證中獲取用戶(Principal), 沒有認證前都代表用戶名
        UserEntity user = (UserEntity) authentication.getPrincipal();
        //前端token刪除需要再前端代碼設定

        //刪除資料庫的token
        ResponseEntity<Void> logout = accountFeignService.logout(user.getAccId());

        //把在filter設置的認證清空
        SecurityContextHolder.clearContext();
        return logout;
    }

    @GetMapping("/refresh")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<UserEntity> refreshToken(){

        //獲取認證
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //從認證中獲取用戶(Principal), 沒有認證前都代表用戶名
        UserEntity user = (UserEntity) authentication.getPrincipal();

        ResponseEntity<UserEntity> userEntityResponseEntity = accountFeignService.refreshToken(user);
        return userEntityResponseEntity;
    }


    @GetMapping("/user")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<UserEntity> getUser(){
        //獲取認證
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //從認證中獲取用戶(Principal), 沒有認證前都代表用戶名
        UserEntity user = (UserEntity) authentication.getPrincipal();

        ResponseEntity<UserEntity> responseEntity = accountFeignService.getUser(user.getUsername());
        return responseEntity;
    }

    @PutMapping("/user")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<UserEntity> updateUser(@RequestBody UpdateEntity updateEntity){
        //獲取認證
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //從認證中獲取用戶(Principal), 沒有認證前都代表用戶名
        UserEntity user = (UserEntity) authentication.getPrincipal();

        updateEntity.setUsername(user.getUsername());
        ResponseEntity<UserEntity> updateUser = accountFeignService.updateUser(updateEntity);
        return updateUser;

    }


    @PutMapping("/password")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<UserEntity> updatePassword(@RequestBody PasswordEntity password){
        //獲取認證
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //從認證中獲取用戶(Principal), 沒有認證前都代表用戶名
        UserEntity user = (UserEntity) authentication.getPrincipal();
        password.setUsername(user.getUsername());
        ResponseEntity<UserEntity> responseEntity = accountFeignService.updatePassword(password);

        return responseEntity;

    }

    @DeleteMapping("/user")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<Void> deleteUser(){
        //獲取認證
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //從認證中獲取用戶(Principal), 沒有認證前都代表用戶名
        UserEntity user = (UserEntity) authentication.getPrincipal();
        accountFeignService.deleteUser(user.getUsername());
        return ResponseEntity.ok().build();
    }


}
