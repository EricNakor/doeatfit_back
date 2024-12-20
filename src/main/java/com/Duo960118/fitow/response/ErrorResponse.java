package com.Duo960118.fitow.response;

import com.Duo960118.fitow.entity.ErrorCodeEnum;
import com.Duo960118.fitow.entity.ErrorResponseBody;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorResponse<T> extends ResponseEntity<ErrorResponseBody<T>> {
    // 1. status
    // 2. message
    // 3. optional: code(http), data(error cause, stack trace)
    private static final String ERROR = "error";

    public ErrorResponse(HttpStatus status, ErrorResponseBody<T> body) {
        super(body, status);
    }

    public static <T> ErrorResponse<T> error(String message, ErrorCodeEnum code, T data){
        ErrorResponseBody<T> body = new ErrorResponseBody<>(ERROR,message,code,data);
        return new ErrorResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,body);
    }

    public static <T>  ErrorResponse<T>  error(ErrorCodeEnum code,T data){
        ErrorResponseBody<T> body = new ErrorResponseBody<>(ERROR,code.getMessage(),code,data);
        return new ErrorResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,body);
    }
}
