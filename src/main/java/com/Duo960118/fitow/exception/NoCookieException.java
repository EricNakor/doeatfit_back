package com.Duo960118.fitow.exception;

import com.Duo960118.fitow.entity.ErrorCodeEnum;

public class NoCookieException extends RuntimeException{
    public static ErrorCodeEnum errorCode = ErrorCodeEnum.COOKIE_NOT_EXIST;
    public NoCookieException(){
        super(errorCode.toString());
    }
    public NoCookieException(String message){
        super(message);
    }
}
