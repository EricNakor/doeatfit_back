package com.Duo960118.fitow.exception;

import com.Duo960118.fitow.entity.ErrorCodeEnum;

public class NoAccessTokenException extends RuntimeException{
    public static ErrorCodeEnum errorCode = ErrorCodeEnum.TOKEN_NOT_EXIST;
    public NoAccessTokenException() {
        super(errorCode.toString());
    }
    public NoAccessTokenException(String message) {
        super(message);
    }
}
