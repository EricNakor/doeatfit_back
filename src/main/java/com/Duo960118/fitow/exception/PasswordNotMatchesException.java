package com.Duo960118.fitow.exception;

import com.Duo960118.fitow.entity.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class PasswordNotMatchesException extends AuthenticationException {
    public static ErrorCodeEnum errorCode = ErrorCodeEnum.PASSWORD_MISMATCH;
    public PasswordNotMatchesException() {
        super(errorCode.getMessage());
    }
    public PasswordNotMatchesException(String message) {
        super(message);
    }
}
