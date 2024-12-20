package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.ErrorCodeEnum;
import com.Duo960118.fitow.exception.PasswordNotMatchesException;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.response.ErrorResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 처리할 예외 클래스 지정
    // ex. null pointer, out of index ... 일일히 다 메서드 각각 지정
    //@ExceptionHandler(NullPointerException.class)

    // 파일 처리 예외(이미지 삭제)
    @ExceptionHandler(IOException.class)
    protected ErrorResponse<Object> handleIOException(Exception ex) {
        return ErrorResponse.error(ex.getMessage(), ErrorCodeEnum.IO_ERROR, ex.getCause());
    }

    // 파일이 존재하지 않을 때
    @ExceptionHandler(FileNotFoundException.class)
    protected ErrorResponse<Object> handleFileNotFound(FileNotFoundException ex) {
        return ErrorResponse.error(ex.getMessage(), ErrorCodeEnum.NOT_FOUND, ex.getCause());
    }

    // 메일 예외
    @ExceptionHandler(MailException.class)
    protected ErrorResponse<Object> handleMailException(MailException ex) {
        return ErrorResponse.error(ex.getMessage(), ErrorCodeEnum.MAIL_ERROR, ex.getCause());
    }
    
    // 자원이 존재하지 않을 때
    @ExceptionHandler(NoSuchElementException.class)
    protected ErrorResponse<Object> handleNoSuchElementException(NoSuchElementException ex) {
        return ErrorResponse.error(ex.getMessage(), ErrorCodeEnum.NOT_FOUND, ex.getCause());
    }

    // 비밀번호가 일치하지 않을 때 (spring security에서 처리하는 인증 말고, 비밀번호 수정이나 회원 탈퇴시 확인하는 경우)
    @ExceptionHandler(PasswordNotMatchesException.class)
    protected ErrorResponse<Object> handlePasswordNotMatchesException(PasswordNotMatchesException ex) {
        return ErrorResponse.error(PasswordNotMatchesException.errorCode, ex.getCause());
    }

    // 파일 용량 초과
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ErrorResponse<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ErrorResponse.error(ex.getMessage(), ErrorCodeEnum.FILE_SIZE_ERROR, null);
    }

    // @Valid
    // Java 꺼
    // request 유효성 검증 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errMessage = new StringBuilder();

        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(":")
                    .append(error.getDefaultMessage())
                    .append("\n");
        }
        return ApiResponse.fail(errMessage);
    }

    // @Validated
    // Spring 꺼
    @ExceptionHandler(ConstraintViolationException.class)
    protected ApiResponse<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errMessage = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errMessage.append("[")
                    .append(violation.getPropertyPath())
                    .append("]")
                    .append(":")
                    .append(violation.getMessage())
                    .append("\n");
//                    .append(Arrays.toString(ex.getStackTrace()));
        }
        return ApiResponse.fail(errMessage);
    }
}