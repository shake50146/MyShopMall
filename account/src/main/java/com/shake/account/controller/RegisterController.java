package com.shake.account.controller;

import com.shake.account.model.entity.AuthEntity;
import com.shake.account.model.entity.RegisterEntity;
import com.shake.account.model.entity.UserEntity;
import com.shake.account.service.AccountAuthService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author: Register
 * @Description 註冊控制器
 * @Date 2023/10/13
 */
@RestController
@RequestMapping("/register")
@Slf4j
@CircuitBreaker(name = "account-breaker-2" , fallbackMethod = "fallback")
public class RegisterController{

    @Autowired
    private AccountAuthService accountAuthService;

    /**
     * 註冊帳號 順便登入
     * @param request
     * @return
     */
    @PostMapping("/account")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterEntity request){

        AuthEntity authEntity = accountAuthService.register(
                request.getUsername(),
                request.getPassword(),
                request.getGender(),
                request.getPhone(),
                request.getEmail(),
                request.getAddress(),
                request.getRole());

        //返回token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " + authEntity.getToken());
        log.info("register");

        return new ResponseEntity<>(authEntity.getUser(),headers, HttpStatus.OK);

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
