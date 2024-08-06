package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.ErrorCodeEnum;
import io.jsonwebtoken.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 처리할 예외 클래스 지정
    // ex. null pointer, out of index ... 일일히 다 메서드 각각 지정
    //@ExceptionHandler(NullPointerException.class)

    // 모든 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleCustomException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<String> handleIOException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}