package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.ErrorCodeEnum;
import com.Duo960118.fitow.entity.ErrorResponseBody;
import com.Duo960118.fitow.entity.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
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
        try{
            // jwt 필터로 부터 예외 코드 추출
            ErrorCodeEnum errorCodeEnum = (ErrorCodeEnum)request.getAttribute(JwtProperties.EXCEPTION_STRING);

            // 없으면(jwt 필터에서 생긴 예외 아님)
            if(errorCodeEnum == null){
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // 에러 response
            ErrorResponseBody<Object> errorResponseBody = ErrorResponseBody.builder()
                    .status("error")
                    .message(errorCodeEnum.getMessage())
                    .code(errorCodeEnum)
                    .data(authException.getMessage())
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            String errorResponseJson = mapper.writeValueAsString(errorResponseBody);

            log.warn(errorCodeEnum.getMessage());
            response.setStatus(errorCodeEnum.getCode());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(errorResponseJson);

        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
}
