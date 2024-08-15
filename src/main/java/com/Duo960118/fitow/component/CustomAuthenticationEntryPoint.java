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
import java.util.Optional;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        try{
            // jwt 필터로 부터 예외 코드 추출
            // 없으면(jwt 필터에서 생긴 예외 아님)
            switch ((ErrorCodeEnum)request.getAttribute(JwtProperties.EXCEPTION_STRING)) {
                case COOKIE_NOT_EXIST -> {
                    log.warn(ErrorCodeEnum.COOKIE_NOT_EXIST.getMessage());
                    response.sendError(ErrorCodeEnum.COOKIE_NOT_EXIST.getCode(), ErrorCodeEnum.COOKIE_NOT_EXIST.getMessage());
                }
                case TOKEN_NOT_EXIST -> {
                    log.warn(ErrorCodeEnum.TOKEN_NOT_EXIST.getMessage());
                    response.sendError(ErrorCodeEnum.TOKEN_NOT_EXIST.getCode(), ErrorCodeEnum.TOKEN_NOT_EXIST.getMessage());
                }
                case EXPIRED_TOKEN -> {
                    //토큰의 유효기간 만료
                    log.warn(ErrorCodeEnum.EXPIRED_TOKEN.getMessage());
                    response.sendError(ErrorCodeEnum.EXPIRED_TOKEN.getCode(), ErrorCodeEnum.EXPIRED_TOKEN.getMessage());
                }
                case INVALID_TOKEN -> {
                    //유효하지 않은 토큰
                    log.warn(ErrorCodeEnum.INVALID_TOKEN.getMessage());
                    response.sendError(ErrorCodeEnum.INVALID_TOKEN.getCode() , ErrorCodeEnum.INVALID_TOKEN.getMessage());
                }
                default -> response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
}
