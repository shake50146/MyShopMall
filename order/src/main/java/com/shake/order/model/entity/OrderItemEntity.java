package com.shake.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Shake
 * @Description OrderDetailEntity 訂單頁中各個訂單
 * @Date 2023/11/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemEntity {

    /**
     *  訂單id : 外鍵 OrderEntity
     */
    private Integer orderId;
    /**
     *  商品id : 外鍵 ProductEntity
     */
    private Integer productId;
    /**
     *  商品名稱
     */
    private String name;
    /**
     *  商品價格
     */
    private Integer price;
    /**
     *  數量
     */
    private Integer quantity;
}
