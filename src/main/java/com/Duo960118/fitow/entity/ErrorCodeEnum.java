package com.Duo960118.fitow.entity;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeEnum {
    COOKIE_NOT_EXIST(HttpServletResponse.SC_UNAUTHORIZED, "쿠키가 존재하지 않습니다."),
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "존재하지 않는 자원입니다."),
    TOKEN_NOT_EXIST(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpServletResponse.SC_UNAUTHORIZED, "잘못된 토큰입니다."),
    EXPIRED_TOKEN(HttpServletResponse.SC_UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_PERMISSION(HttpServletResponse.SC_UNAUTHORIZED, "사용자가 권한이 없습니다."),
    USERNAME_NOT_FOUND(HttpServletResponse.SC_UNAUTHORIZED,"사용자를 찾을 수 없습니다."),
    FORBIDDEN_REQUEST(HttpServletResponse.SC_FORBIDDEN, "ADMIN 회원만 접근할 수 있습니다."),
    PASSWORD_MISMATCH(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"비밀번호가 일치하지 않습니다."),
    MAIL_ERROR(1000,"메일을 보내는데 문제가 생겼습니다."),
    IO_ERROR(1001,"파일 입출력에 문제가 생겼습니다"),
    FILE_SIZE_ERROR(1002,"파일 사이즈는 50mb 이하만 지원합니다."),
    TEST_ERROR(9999,"테스트");

    private final int code;
    private final String message;
}