package com.shake.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: PasswordRequest
 * @Description
 * @Date 2023/11/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
