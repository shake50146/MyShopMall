package com.shake.account.controller;

import com.shake.account.model.dto.AuthRequest;
import com.shake.account.model.entity.AuthEntity;
import com.shake.account.model.entity.UserEntity;
import com.shake.account.service.AccountAuthService;
import com.shake.account.service.AccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: JwtController
 * @Description 認證控制器
 * @Date 2023/10/6
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@CircuitBreaker(name = "account-breaker-2" , fallbackMethod = "fallback")
public class AuthController {

    @Autowired
    private AccountAuthService accountAuthService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestBody AuthRequest authRequest){
        //生成token
        AuthEntity authEntity = accountAuthService.authenticationLogin(authRequest.getUsername(), authRequest.getPassword());
        //把token放入 響應頭中返回
        HttpHeaders headers = new HttpHeaders();
        //在token 前添加Bearer
        headers.set("Authorization","Bearer " + authEntity.getToken());
        log.info("login");

        return new ResponseEntity<>(authEntity.getUser(),headers, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String username){
        //從資料庫刪除token
        accountAuthService.deleteToken(username);
        log.info("logout");
        return ResponseEntity.ok().body("Logout Success");
    }

    @PutMapping("/token")
    public ResponseEntity<UserEntity> refreshToken(@RequestBody String username){
        //更新資料庫token
        AuthEntity authEntity = accountAuthService.refreshToken(username);

        //把token放入 響應頭中返回
        HttpHeaders headers = new HttpHeaders();
        //在token 前添加Bearer
        headers.set("Authorization","Bearer " + authEntity.getToken());
        log.info("refresh ok");
        return new ResponseEntity<>(authEntity.getUser(),headers,HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken(@RequestParam String username){
        String token = accountAuthService.getToken(username);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/user")
    public ResponseEntity<UserEntity> getAuthUser(@RequestParam String username){
        UserEntity user = accountService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * 熔斷機制需要的fallback方法 自定義返回調用其他服務錯誤信息
     * @param throwable
     * @return
     */
    public ResponseEntity<Object> fallback(Throwable throwable) {
        //默認錯誤信息, 表示資料庫出錯
        String message = "MySQL Service Error";
        if(throwable != null){
            //獲取Service端的錯誤信息, 如果有則使用該信息
            message = throwable.getMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(message);
    }

}
