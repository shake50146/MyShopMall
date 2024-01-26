package com.shake.order.controller;

import com.shake.order.model.entity.OrderEntity;
import com.shake.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: OrderController
 * @Description
 * @Date 2023/11/2
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/list")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<OrderEntity> showOrderList(@RequestParam String username){

        OrderEntity orderEntity = orderService.listOrders(username);


        return ResponseEntity.ok(orderEntity);
    }
}
