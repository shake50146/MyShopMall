package com.shake.authentication.feign;

import com.shake.authentication.model.entity.LoginEntity;
import com.shake.authentication.model.entity.RegisterEntity;
import com.shake.authentication.model.entity.UserEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: accountFeignService
 * @Description
 * @Date 2023/11/4
 */
@FeignClient(name = "shop-account")
@CircuitBreaker(name = "auth-breaker-2" , fallbackMethod = "fallback")
public interface AccountFeignService {

    @GetMapping("/auth/user")
    ResponseEntity<UserEntity> getAuthUser(@RequestParam String username);

    @PostMapping("/register/account")
    ResponseEntity<UserEntity> register(@RequestBody RegisterEntity registerEntity);

    @PostMapping("/auth/login")
    ResponseEntity<UserEntity> login(@RequestBody LoginEntity loginEntity);

    @GetMapping("/auth/logout")
    ResponseEntity<String> logout(@RequestParam String username);

    @PutMapping("/auth/token")
    ResponseEntity<UserEntity> refreshToken(@RequestBody String username);

    @GetMapping("/auth/token")
    ResponseEntity<String> getToken(@RequestParam String username);


    /**
     * 熔斷機制需要的fallback方法 自定義返回調用其他服務錯誤信息
     * @param throwable
     * @return
     */
    default ResponseEntity<Object> fallback(Throwable throwable) {
        //默認錯誤信息, 表示Account服務出錯
        String message = "Account Service Error";
        if(throwable != null){
            //獲取Account服務的錯誤信息, 如果有則使用該信息
            message = throwable.getMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(message);
    }

}
