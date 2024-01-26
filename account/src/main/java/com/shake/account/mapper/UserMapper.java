package com.shake.account.mapper;

import com.shake.account.model.entity.UserEntity;
import com.shake.account.model.dto.UpdateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author: UserMapper
 * @Description 用戶映射
 * @Date 2023/10/13
 */
@Mapper
public interface UserMapper   {

    /**
     * 以用戶名查詢用戶信息
     * @param username
     * @return UserEntity
     */
    UserEntity selectUserPasswordByUsername(@Param("username") String username);

    /**
     * 以用戶名查詢用戶 不含密碼
     * @param username
     * @return String
     */
    UserEntity selectUserByUsername(@Param("username") String username);

    /**
     * 以用戶名查詢用戶id
     * @param username
     * @return
     */
    Integer selectAccIdByUsername(@PathVariable("username") String username);

    /**
     * 以用戶名查詢Role
     * @param username
     * @return List<String>
     */
    List<String> listRoleByUsername(@Param("username") String username);

    /**
     * 更新用戶(密碼以外)
     * @param request
     * @return Integer
     */
    Integer updateUserByRequest(UpdateRequest request);

    /**
     * 更新密碼
     * @param password
     * @return
     */
    Integer updatePasswordByAccId(@Param("password")String password,@Param("accId")Integer accId);

    /**
     * 刪除用戶
     * @param username
     * @return Integer
     */
    Integer deleteUserByUsername(String username);

}
