package com.Duo960118.fitow.entity;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeEnum {
    COOKIE_NOT_EXIST(HttpServletResponse.SC_UNAUTHORIZED, "쿠키가 존재하지 않습니다."),
    TOKEN_NOT_EXIST(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpServletResponse.SC_UNAUTHORIZED, "잘못된 토큰입니다."),
    EXPIRED_TOKEN(HttpServletResponse.SC_UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_PERMISSION(HttpServletResponse.SC_UNAUTHORIZED, "사용자가 권한이 없습니다."),
    USERNAME_NOT_FOUND(HttpServletResponse.SC_UNAUTHORIZED,"사용자를 찾을 수 없습니다."),
    FORBIDDEN_REQUEST(HttpServletResponse.SC_FORBIDDEN, "ADMIN 회원만 접근할 수 있습니다."),
    TEST(HttpServletResponse.SC_NO_CONTENT, "Test");

    private final int httpStatus;
    private final String message;
}