package com.shake.gateway.feign;

import com.shake.gateway.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: accountFeignService
 * @Description
 * @Date 2023/11/4
 */
@FeignClient(name = "shop-account")
public interface AccountFeignService {

    @GetMapping("/user/account")
    ResponseEntity<UserEntity> getUser(@RequestParam String username);

    @PutMapping("/user/account")
    ResponseEntity<UserEntity> updateUser(@RequestBody UpdateEntity updateEntity);

    @PutMapping("/user/password")
    ResponseEntity<UserEntity> updatePassword(@RequestBody PasswordEntity passwordEntity);

    @DeleteMapping("/user/account")
    ResponseEntity<Void> deleteUser(@RequestParam String username);

    @PostMapping("/register/account")
    ResponseEntity<UserEntity> register(@RequestBody RegisterEntity registerEntity);

    @PostMapping("/auth/login")
    ResponseEntity<UserEntity> login(@RequestBody LoginEntity loginEntity);

    @GetMapping("/auth/logout")
    ResponseEntity<Void> logout(@RequestParam Integer accId);

    @PutMapping("/auth/token")
    ResponseEntity<UserEntity> refreshToken(@RequestBody UserEntity userEntity);

    @GetMapping("/auth/token")
    ResponseEntity<String> getToken(@RequestParam String username);



}
