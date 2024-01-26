package com.shake.account.model.dto;

import com.shake.common.enums.UserRoleConstantEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author: UpdateRequest
 * @Description 更新請求對象
 * @Date 2023/10/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequest {

    private String username;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private UserRoleConstantEnum role;
}
