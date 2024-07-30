package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.JwtErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Setter
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn(JwtErrorCode.FORBIDDEN_REQUEST.getMessage());
        response.sendError(JwtErrorCode.FORBIDDEN_REQUEST.getHttpStatus(), JwtErrorCode.FORBIDDEN_REQUEST.getMessage());
    }
}
