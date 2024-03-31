package com.Duo960118.fitow.filter;

import com.Duo960118.fitow.component.TokenUtil;
import com.Duo960118.fitow.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;

    //  @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 email, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // 아직은 권한이 null인 Authentication을 생성해준다.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, password, null
        );

        // AuthenticationManager에게 token의 유효성 검증을 부탁
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws UnsupportedEncodingException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        // 중복 로그인 체크
        String email = customUserDetails.getUsername();
        if (tokenUtil.isLogin(email)) {
            // 지우기 전에 기 로그인된 액세스 토큰 블랙리스트 추가
            tokenUtil.blacklistAccessToken(tokenUtil.findAccessToken(email));
            tokenUtil.deleteRefreshToken(email);
        }
        // cookie에 access token 저장
        String accessToken = tokenUtil.createAccessToken(customUserDetails);
        logger.debug(accessToken);

        Cookie accessCookie = tokenUtil.saveAccessTokenAsCookie(accessToken);

        response.addCookie(accessCookie);

        // redis에 refresh token 저장
        tokenUtil.saveRefreshToken(accessToken);

        // ok
        response.setStatus(200);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }
}