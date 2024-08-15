package com.Duo960118.fitow.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseBody<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final ErrorCodeEnum code;
    private final T data;
}