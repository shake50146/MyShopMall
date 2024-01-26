package com.shake.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: OrderServiceFeignClient
 * @Description
 * @Date 2023/11/3
 */
@FeignClient(name = "shop-account")
public interface AccountFeignService {

    @PostMapping("/user/get/{username}")
    Integer getAccIdByUsername(@PathVariable("username") String username);
}
