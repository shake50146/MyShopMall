package com.shake.account.model.entity;

import com.shake.common.enums.UserRoleConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: User
 * @Description 用戶實體
 * @Date 2023/10/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity{

    private Integer accId;
    /**
     * 用戶名
     */
    private String username;
    /**
     * 用戶密碼
     */
    private String password;
    /**
     * 性別
     */
    private String gender;
    /**
     * 手機號碼
     */
    private String phone;
    /**
     * 電子郵件
     */
    private String email;
    /**
     * 地址
     */
    private String address;
    /**
     * 角色
     */
    private UserRoleConstantEnum role;
    /**
     * 認證令牌
     */
    private String token;


}
