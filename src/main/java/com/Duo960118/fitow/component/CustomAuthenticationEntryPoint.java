package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.ErrorCodeEnum;
import com.Duo960118.fitow.entity.JwtProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        switch ((ErrorCodeEnum)request.getAttribute(JwtProperties.EXCEPTION_STRING)) {
            case COOKIE_NOT_EXIST -> {
                log.warn(ErrorCodeEnum.TOKEN_NOT_EXIST.getMessage());
                response.sendError(ErrorCodeEnum.COOKIE_NOT_EXIST.getHttpStatus(), ErrorCodeEnum.COOKIE_NOT_EXIST.getMessage());
            }
            case TOKEN_NOT_EXIST -> {
                log.warn(ErrorCodeEnum.TOKEN_NOT_EXIST.getMessage());
                response.sendError(ErrorCodeEnum.TOKEN_NOT_EXIST.getHttpStatus(), ErrorCodeEnum.TOKEN_NOT_EXIST.getMessage());
            }
            case EXPIRED_TOKEN -> {
                //토큰의 유효기간 만료
                log.warn(ErrorCodeEnum.EXPIRED_TOKEN.getMessage());
                response.sendError(ErrorCodeEnum.EXPIRED_TOKEN.getHttpStatus(), ErrorCodeEnum.EXPIRED_TOKEN.getMessage());
            }
            case INVALID_TOKEN -> {
                //유효하지 않은 토큰
                log.warn(ErrorCodeEnum.INVALID_TOKEN.getMessage());
                response.sendError(ErrorCodeEnum.INVALID_TOKEN.getHttpStatus() , ErrorCodeEnum.INVALID_TOKEN.getMessage());
            }
            default -> response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
}
