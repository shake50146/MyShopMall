package com.shake.product.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Product
 * @Description 商品對象
 * @Date 2023/10/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {

    /**
     * 商品ID
     */
    private Integer productId;
    /**
     * 名稱
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 價格
     */
    private Integer price;
    /**
     * 庫存
     */
    private Integer stock;
    /**
     * 分類
     */
    private Integer categoryId;
    /**
     * 圖片
     */
    private String images;
    /**
     * 商品上下架狀態
     */
    private String status;
    /**
     * 商品創建時間
     */
    private Date createdTimestamp;
    /**
     * 商品最後更新時間
     */
    private Date updatedTimestamp;
}
