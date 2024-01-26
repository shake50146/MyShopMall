package com.shake.gateway.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author: RouterValidator
 * @Description
 * @Date 2023/11/17
 */
@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of("/auth/register");

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
