package com.shake.account.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Shake
 * @Description AuthEntity
 * @Date 2023/12/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthEntity {
    private UserEntity user;
    private String token;
}
