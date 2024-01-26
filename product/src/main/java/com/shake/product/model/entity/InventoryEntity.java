package com.shake.product.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: InventoryEntity
 * @Description 庫存對象
 * @Date 2023/10/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryEntity {
    /**
     * 庫存id
     */
    private Integer invId;
    /**
     * 庫存所屬商品id
     */
    private Integer pId;
    /**
     * 可用庫存
     */
    private Integer availableStock;
    /**
     * 已預訂未支付庫存
     */
    private Integer reservedStock;
}
