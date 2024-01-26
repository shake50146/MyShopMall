package com.shake.product.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: ReviewAndRatingEntity
 * @Description 評價與評論對象
 * @Date 2023/10/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAndRatingEntity {

    /**
     * 評論id
     */
    private Integer revId;
    /**
     * 發表評論的用戶id
     */
    private Integer uId;
    /**
     * 內容
     */
    private String content;
    /**
     * 評分
     */
    private Integer rating;
    /**
     * 發表時間
     */
    private Date timestamp;
}
