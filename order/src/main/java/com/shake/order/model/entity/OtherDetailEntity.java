package com.shake.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Shake
 * @Description OtherDetailEntity 訂單內其他信息
 * @Date 2023/11/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtherDetailEntity {
    /**
     * 用戶ID 外鍵 : UserEntity
     */
    private Integer accId;
    /**
     *  訂單id : 外鍵 OrderEntity
     */
    private Integer orderId;
    /**
     * 手機號碼
     */
    private String phone;
    /**
     * 地址
     */
    private String address;
}
