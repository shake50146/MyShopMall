package com.shake.account.exception;

/**
 * @author: UsernameException
 * @Description 自訂義異常
 * @Date 2023/10/16
 */

public class UsernameException extends RuntimeException{

    /**
     * 用戶名已存在異常
     */
    public UsernameException(String message){
        super(message);
    }
}
