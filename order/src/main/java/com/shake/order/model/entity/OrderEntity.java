package com.shake.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author:
 * @Description orderEntity 訂單概況
 * @Date 2023/11/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {

    /**
     * 訂單id 主鍵
     */
    private Integer orderId;
    /**
     * 會員id 外鍵 UserEntity
     */
    private Integer accId;
    /**
     * 下單時間
     */
    private Date orderTime;
    /**
     * 訂單狀態
     */
    private String status;
    /**
     * 總金額
     */
    private Integer totalPrice;

}
