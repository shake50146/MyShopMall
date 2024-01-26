package com.shake.gateway.entity;

import com.shake.common.enums.UserRoleConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: RegisterEntity
 * @Description
 * @Date 2023/11/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterEntity {

    private String username;
    private String password;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private UserRoleConstantEnum role;
}
