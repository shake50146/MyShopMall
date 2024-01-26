package com.shake.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: TokenEntity
 * @Description
 * @Date 2023/11/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenEntity {
    private String username;
    private String token;
}
