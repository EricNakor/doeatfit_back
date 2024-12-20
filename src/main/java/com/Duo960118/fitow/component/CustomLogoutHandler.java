package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.JwtProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler, LogoutSuccessHandler {
    private final TokenUtil tokenUtil;

    // spring security logout filter의 logout handler
    // 로그아웃 시에는 리프레시 토큰을 제거하고, 액세스 토큰을 액세스 토큰의 유효기간 만큼 ttl을 걸어서 블랙리스트에 추가
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = tokenUtil.resolveToken(request, JwtProperties.ACCESS_TOKEN_KEY);
        String email;
        try {
            email = tokenUtil.getEmailFromAccessToken(accessToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // refresh token 제거
        tokenUtil.deleteRefreshToken(email);

        // access token blacklist 추가
        tokenUtil.blacklistAccessToken(accessToken);
    }

    // spring security logout filter의 logout success handler
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // http 응답코드 200 : ok
        response.setStatus(200);
        // response.sendRedirect("/");
        // react에서는 3000포트를 사용하고 spring에서는 8080을 바라보는데
        // 서버단인 spring에서 처리되지 않고 client에 8080으로 가라고 명령을 내려 cors 이슈가 발생
        // https://okky.kr/questions/1451753
    }
}
