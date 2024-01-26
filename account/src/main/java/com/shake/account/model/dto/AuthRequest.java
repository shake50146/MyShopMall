package com.shake.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author: AuthRequest
 * @Description 認證請求對象
 * @Date 2023/10/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {

    private String username;
    private String password;
}
