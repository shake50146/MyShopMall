package com.shake.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: LoginEntity
 * @Description
 * @Date 2023/11/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginEntity {
    private String username;
    private String password;
}
