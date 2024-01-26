package com.shake.portal.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author: Shake
 * @Description RouterValidator 確認請求路徑開頭是否以valid開頭 代表需要認證
 * @Date 2023/11/28
 */
@Component
public class RouterValidator {

    /**
     * 定義開放的API端點
     */
    public static final List<String> openApiEndpoints = List.of(
            "/valid"
    );

    /**
     * 定義一個謂詞Predicate 用於判斷是否需要進行安全驗證
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().startsWith(uri));
}
