package com.shake.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Shake
 * @Description PaymentEntity 支付信息表
 * @Date 2023/11/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEntity {

    /**
     *  訂單id : 外鍵 OrderEntity
     */
    private Integer orderId;
    /**
     *  付款狀態
     */
    private String status;

}
