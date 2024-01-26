package com.shake.account.exception;

/**
 * @author: AdminException
 * @Description
 * @Date 2023/11/21
 */
public class AdminException extends RuntimeException{
    /**
     * 權限不足異常
     */
    public AdminException(){super("Insufficient authority");}

}
