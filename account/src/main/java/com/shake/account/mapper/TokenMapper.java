package com.shake.account.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author: TokenMapper
 * @Description 令牌映射
 * @Date 2023/11/1
 */
@Mapper
public interface TokenMapper {

    /**
     * 新增token
     * @param username
     * @param token
     * @return
     */
    Integer insertTokenByUsername(String username , String token);

    /**
     * 以用戶名查詢token
     * @param username
     * @return
     */
    String selectTokenByUsername(String username);

    /**
     * 刷新token
     * @param username
     * @param token
     * @return
     */
    Integer updateTokenByUsername(String username , String token);

    /**
     * 刪除token
     * @param accId
     * @return
     */
    Integer deleteTokenByAccId(Integer accId);

}
