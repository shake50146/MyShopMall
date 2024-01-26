package com.shake.common.exception;

/**
 * @author: ErrorCodeEnum
 * @Description 錯誤代碼枚舉類
 * @Date 2023/10/16
 */

public enum ErrorCodeEnum {

    UNKNOWN_EXCEPTION(10000,"Unknown System Error"),
    USER_EXISTS_EXCEPTION(10001,"User Exists"),
    NO_STOCK_EXCEPTION(10002,"Product is out of stock"),
    LOGIN_EXCEPTION(10003,"Username or Password Error");

    private Integer code;

    private String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
