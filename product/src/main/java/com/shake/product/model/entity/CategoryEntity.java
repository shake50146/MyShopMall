package com.shake.product.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author: CategoryEntity
 * @Description 分類對象
 * @Date 2023/10/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryEntity {
    /**
     * 分類id
     */
    private Integer categoryId;
    /**
     * 分類名稱
     */
    private String name;
    /**
     * 父分類id
     */
    private Integer parentCid;
}
