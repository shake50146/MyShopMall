package com.shake.order;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.shake.order.model.mapper")
@OpenAPIDefinition(info = @Info(
        title = "Bill account test API",
        description = "This is my first test API ",
        version = "1.0.0"
))
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
        log.info("----------------------------------");
        log.info("Test url : http://localhost:8083/");
        log.info("Swagger url : http://localhost:8083/swagger-ui/index.html ");
    }

}
