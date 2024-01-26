package com.shake.gateway.entity;

import com.shake.common.enums.UserRoleConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: UpdateEntity
 * @Description
 * @Date 2023/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEntity {

    private String username;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private UserRoleConstantEnum role;
}
