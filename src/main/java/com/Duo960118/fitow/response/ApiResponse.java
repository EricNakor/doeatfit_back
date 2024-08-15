package com.Duo960118.fitow.response;

import com.Duo960118.fitow.entity.ApiResponseBody;
import lombok.Getter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> extends ResponseEntity<Object> {
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public ApiResponse(HttpStatus status, ApiResponseBody<T> body) {
        super(body, status);
    }

    public ApiResponse(HttpStatus status, Resource body) {
        super(body, status);
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponseBody<T> body = new ApiResponseBody<>(SUCCESS, data);
        return new ApiResponse<>(HttpStatus.OK, body);
    }

    public static ApiResponse<Resource> successResource(Resource data) {
        return new ApiResponse<>(HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> fail(T data) {
        ApiResponseBody<T> body = new ApiResponseBody<>(FAIL, data);
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, body);
    }
}