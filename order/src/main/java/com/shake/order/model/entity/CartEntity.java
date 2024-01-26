package com.shake.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Shake
 * @Description CartEntity 購物車的資料
 * @Date 2023/11/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartEntity {
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
