package com.shake.account.feign;

import com.shake.account.model.entity.UserEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: portalFeignService
 * @Description
 * @Date 2023/11/9
 */
@FeignClient(name = "shop-auth")
public interface AuthFeignService {

    @PostMapping("/security/auth/token")
    @CircuitBreaker(name = "account-breaker-2" , fallbackMethod = "fallback")
    ResponseEntity<String> generateToken(@RequestBody UserEntity userEntity);


    /**
     * 熔斷機制需要的fallback方法 自定義返回調用其他服務錯誤信息
     * @param throwable
     * @return
     */
    default ResponseEntity<Object> fallback(Throwable throwable) {
        //默認錯誤信息, 表示Authentication服務出錯
        String message = "Authentication Service Error";
        if(throwable != null){
            //獲取Authentication服務的錯誤信息, 如果有則使用該信息
            message = throwable.getMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(message);
    }

}
