package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.JwtErrorCode;
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
        switch ((JwtErrorCode)request.getAttribute(JwtProperties.EXCEPTION_STRING)) {
            case COOKIE_NOT_EXIST -> {
                log.warn(JwtErrorCode.TOKEN_NOT_EXIST.getMessage());
                response.sendError(JwtErrorCode.COOKIE_NOT_EXIST.getHttpStatus(), JwtErrorCode.COOKIE_NOT_EXIST.getMessage());
            }
            case TOKEN_NOT_EXIST -> {
                log.warn(JwtErrorCode.TOKEN_NOT_EXIST.getMessage());
                response.sendError(JwtErrorCode.TOKEN_NOT_EXIST.getHttpStatus(), JwtErrorCode.TOKEN_NOT_EXIST.getMessage());
            }
            case EXPIRED_TOKEN -> {
                //토큰의 유효기간 만료
                log.warn(JwtErrorCode.EXPIRED_TOKEN.getMessage());
                response.sendError(JwtErrorCode.EXPIRED_TOKEN.getHttpStatus(), JwtErrorCode.EXPIRED_TOKEN.getMessage());
            }
            case INVALID_TOKEN -> {
                //유효하지 않은 토큰
                log.warn(JwtErrorCode.INVALID_TOKEN.getMessage());
                response.sendError(JwtErrorCode.INVALID_TOKEN.getHttpStatus() ,JwtErrorCode.INVALID_TOKEN.getMessage());
            }
            default -> response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
}
