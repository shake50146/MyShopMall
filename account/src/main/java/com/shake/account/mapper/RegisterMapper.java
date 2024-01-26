package com.shake.account.mapper;

import com.shake.account.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: RegisterMapper
 * @Description 註冊映射
 * @Date 2023/11/1
 */
@Mapper
public interface RegisterMapper {

    /**
     * 新增用戶
     * @param user
     * @return Integer
     */
    Integer insertUser(UserEntity user);

    /**
     * 新增用戶密碼
     * @param password
     * @param username
     * @return
     */
    Integer insertPassword(@Param("password")String password, @Param("username")String username);
}
