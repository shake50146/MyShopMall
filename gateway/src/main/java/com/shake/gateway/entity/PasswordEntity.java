package com.shake.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: PasswordEntity
 * @Description
 * @Date 2023/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordEntity {
    private String username;
    private String oldPassword;
    private String newPassword;
}
