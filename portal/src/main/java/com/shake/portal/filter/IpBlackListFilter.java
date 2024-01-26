package com.shake.portal.filter;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;

/**
 * @author: Shake
 * @Description IpBlackListFilter
 * @Date 2023/12/9
 */
@Component
@Slf4j
public class IpBlackListFilter implements GlobalFilter , Ordered {
    private static final String BLACKLIST_KEY = "blacklist:";
    private static final Integer MAX_COUNT = 10;
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String HEADER_X_FORWARDED_FOR = "X_Forwarded_For";
    private static final String HEADER_PROXY_CLIENT_IP = "Proxy_Client_IP";
    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL_Proxy_Client_IP";
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //獲取客戶端真實IP
        String clientIpAddress = this.getRealIpAddress(request);

        //檢查當前請求的IP是否在黑名單中
        if(redisTemplate.hasKey(BLACKLIST_KEY+clientIpAddress)){
            log.info("IpAddress has been blocked by 1 day");
            return this.onError(exchange,HttpStatus.FORBIDDEN,"IpAddress has been blocked by 1 day");
        }
        //使用redisTemplate方法獲取key對應的count次數 判斷為空的話就附值為0 數值超過10的話加入黑名單
        //如果有值的話再把key跟count+1 存進redisTemplate
        String key = clientIpAddress;
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        Integer addressCount = valueOperations.get(key);
        if(addressCount == null ){
            addressCount = 0;
        }else if(addressCount > MAX_COUNT){
            //如果該IP 1分鐘內連續請求超過10次設置限時IP黑名單
            redisTemplate.opsForValue().set(BLACKLIST_KEY+clientIpAddress,"block", Duration.ofDays(1));
            log.info("Too many requests");
            return this.onError(exchange,HttpStatus.FORBIDDEN,"Too many requests");
        }
        //計算當前請求的客戶端IP一分鐘內請求的次數, 設置一分儲存一分鐘, 過時重新計數
        redisTemplate.opsForValue().set(key,addressCount+1,Duration.ofMinutes(1));
        return chain.filter(exchange);
    }


    /**
     *  獲取客戶端真實IP
     *
     *  XForwardedRemoteAddressResolver.maxTrustedIndex(1)
     *      : 該方法也是等同request.getHeaders().getFirst(HEADER_X_FORWARDED_FOR)
     * @param request
     * @return
     */
    public static String getRealIpAddress(ServerHttpRequest request){
        String ipAddress;

        try {
            //1. 根據常見的代理服務器轉發的請求IP存放協議, 從請求頭中獲取原始請求IP
            ipAddress = request.getHeaders().getFirst(HEADER_X_FORWARDED_FOR);
            if(StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)){
                ipAddress = request.getHeaders().getFirst(HEADER_PROXY_CLIENT_IP);
            }
            if(StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)){
                ipAddress = request.getHeaders().getFirst(HEADER_WL_PROXY_CLIENT_IP);
            }

            //2.如果沒有轉發的IP, 則獲取當前通信的請求端的IP
            if(StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)){
                InetSocketAddress remoteAddress = request.getRemoteAddress();
                if(remoteAddress != null){
                    ipAddress = remoteAddress.getAddress().getHostAddress();
                }

                //如果是127.0.0.1, 則獲取本地IP
                if(LOCALHOST.equals(ipAddress) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)){
                    InetAddress localHost = InetAddress.getLocalHost();
                    ipAddress = localHost.getHostAddress();
                }
            }
            //對於通過多個代理的情況, 第一個IP為客戶端的真實IP, 多個IP按照","劃分
            if(ipAddress != null){
                ipAddress = ipAddress.split(",")[0].trim();
            }
        }catch (Exception e){
            throw new RuntimeException("Parse IP failed");
        }
        return ipAddress == null ? "" : ipAddress;

    }

    /**
     * 錯誤返回: 在響應體中響應錯誤訊息
     * @param exchange
     * @param httpStatus
     * @param errorMessage
     * @return
     */
    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus,String errorMessage){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");

        String body = "{\"error\": \"" + errorMessage + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -201;
    }
}
