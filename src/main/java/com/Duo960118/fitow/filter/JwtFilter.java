package com.Duo960118.fitow.filter;

import com.Duo960118.fitow.component.TokenUtil;
import com.Duo960118.fitow.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info(request.getRequestURL().toString());

        // 쿠키(혹은 헤더)에서 access token,refresh token을 받아옵니다.
        String accessToken = tokenUtil.resolveToken((HttpServletRequest) request, TokenUtil.ACCESS_TOKEN_KEY);
        String email = "";
        if (!accessToken.isBlank()) {
            email = tokenUtil.getEmailFromAccessToken(accessToken);
        }

        // 액세스 토큰이 유효한지 확인
        if (!accessToken.isBlank() && tokenUtil.validateAccessToken(accessToken)) {
            logger.info("access token 유효");
            // 액세스 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = tokenUtil.getAuthentication(accessToken);

            // 리프레시 토큰이 만료 됐으면 재발급
            if (!tokenUtil.hasRefreshToken(email,accessToken)) {
                tokenUtil.saveRefreshToken(accessToken);
            }

            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        logger.info("access token 무효");

        // redis에서 access token을 key로 갖는 refresh token이 있는지 확인합니다
        if (!accessToken.isBlank() && tokenUtil.hasRefreshToken(email,accessToken)) {
            logger.info("refresh token 존재. access token 재발급");
            // 리프레시 토큰이 유효하면 액세스 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = tokenUtil.createAuthFromExpiredToken(accessToken);

            // 액세스 토큰 재발급
            String newAccessToken = tokenUtil.createAccessToken((CustomUserDetails) authentication.getPrincipal());

            // 리프레시 토큰 키값 변경
            tokenUtil.renameRefreshToken(email, newAccessToken);

            // cookie에 저장
            Cookie accessCookie = tokenUtil.saveAccessTokenAsCookie(newAccessToken);

            response.addCookie(accessCookie);

            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}