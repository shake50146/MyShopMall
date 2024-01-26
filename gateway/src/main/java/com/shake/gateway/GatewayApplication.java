package com.shake.gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Bill portal test API",
        description = "This is my first test API ",
        version = "1.0.0"
))
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        log.info("----------------------------------");
        log.info("Test url : http://localhost:8081/");
        log.info("Swagger url : http://localhost:8081/swagger-ui/index.html ");
        log.info("Swagger WebFlux url : http://localhost:8081/v3/api-docs ");
    }

}
