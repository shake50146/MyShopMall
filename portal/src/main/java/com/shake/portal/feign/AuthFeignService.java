package com.shake.portal.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author: Shake
 * @Description AuthFeignService
 * @Date 2023/11/24
 */
@FeignClient(name = "shop-auth")
public interface AuthFeignService {

    @PostMapping("/security/get/token")
    @CircuitBreaker(name = "portal-breaker-2" , fallbackMethod = "fallback")
    ResponseEntity<String> parseToken(@RequestHeader("Authorization") String token);

    /**
     * 熔斷機制需要的fallback方法 自定義返回調用其他服務錯誤信息
     * @param throwable
     * @return
     */
    default ResponseEntity<Object> fallback(Throwable throwable) {
        String message = "Authentication Service Error";
        if(throwable != null){
            message = throwable.getMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(message);
    }
}
