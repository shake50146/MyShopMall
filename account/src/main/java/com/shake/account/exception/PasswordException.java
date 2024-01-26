package com.shake.account.exception;

/**
 * @author: Shake
 * @Description PasswordException
 * @Date 2023/12/16
 */
public class PasswordException extends RuntimeException {
    /**
     * 密碼錯誤異常
     */
    public PasswordException(){super("Password Error");}
}
