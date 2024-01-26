package com.shake.product.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: RecommendationEntity
 * @Description 推薦對象
 * @Date 2023/10/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationEntity {
    /**
     * 推薦id
     */
    private Integer recId;
    /**
     * 推薦內容
     */
    private String content;
    /**
     * 推薦類型
     */
    private String type;
}
