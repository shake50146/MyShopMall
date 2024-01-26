package com.shake.portal.filter;

import com.shake.portal.config.RouterValidator;
import com.shake.portal.feign.AuthFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author: Shake
 * @Description AuthGlobalFilter
 * @Date 2023/11/27
 */
@Component
@Slf4j
public class TokenAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    @Lazy
    private AuthFeignService authFeignService;

    @Autowired
    private  RouterValidator routerValidator;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        //確認請求地址是否包含valid, 沒有包含就直接放行
        if(!routerValidator.isSecured.test(request)){
            return chain.filter(exchange);
        }

        final String bearerToken = this.getAuthHeader(request);
        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            //解析去掉Bearer前綴的 Token
            String frontToken = bearerToken.substring(7);
            //解析Token, 獲取username
            String username = authFeignService.parseToken(frontToken).getBody();
            //將認證對象放到請求header中,讓後續的請求處理能夠調用
            //把去除Bearer開頭的token重新存進請求頭, 以免後續請求使用到有Bearer的token
            ServerHttpRequest newRequest = request.mutate()
                    .header("Username", username)
                    .header("Authorization",frontToken).build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        }else {
            //Token 沒填或是不是Bearer開頭的都會報此錯誤
            return this.onError(exchange, HttpStatus.UNAUTHORIZED,"Portal : Authentication token is null");
        }
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

    /**
     * 獲取請求頭Authorization list的token
     * @param request
     * @return
     */
    public String getAuthHeader(ServerHttpRequest request){
        List<String> authorizationHeaders = request.getHeaders().getOrEmpty("Authorization");
        return authorizationHeaders.isEmpty() ? null : authorizationHeaders.get(0);
    }

    /**
     * 數字越小優先度越高, 放在IP黑名單後面,確認IP沒問題後才確認token與路徑
     * @return
     */
    @Override
    public int getOrder() {
        return -200;
    }
}
