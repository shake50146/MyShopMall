package com.shake.portal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: Shake
 * @Description IpKeyResolver 配置自定義的KeyResolver, 使用請求客戶端的IP作為限流方法
 * @Date 2023/12/8
 */
@Component
@Slf4j
public class IpKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        // 根据请求的远程IP地址作为限流Key
        String ipAddress = exchange.getRequest().getRemoteAddress().getHostString();
        log.info("Request IP : " + ipAddress);
        return Mono.just(ipAddress);
    }
}
