package com.shake.order.service;

import com.shake.order.feign.AccountFeignService;
import com.shake.order.model.entity.OrderEntity;
import com.shake.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: OrderService
 * @Description
 * @Date 2023/11/2
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AccountFeignService accountFeignService;

    public OrderEntity listOrders(String username){

        Integer id = accountFeignService.getAccIdByUsername(username);
        OrderEntity orderEntity = OrderEntity.builder()
                .accId(id)
                .build();
        return orderEntity;
    }
}
