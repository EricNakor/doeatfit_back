package com.Duo960118.fitow.filter;

import com.Duo960118.fitow.component.TokenUtil;
import com.Duo960118.fitow.entity.CustomUserDetails;
import com.Duo960118.fitow.entity.JwtErrorCode;
import com.Duo960118.fitow.entity.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private String token="", email="";
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        logger.info(request.getRequestURL().toString());

        // 쿠키에서 access token을 받아옵니다.
        try {
            // request에서 token을 가져옵니다
            token = tokenUtil.resolveToken(request, JwtProperties.ACCESS_TOKEN_KEY);

            // token에 있는 이메일을 가져옵니다
            email = tokenUtil.getEmailFromAccessToken(token);

            if (tokenUtil.validateAccessToken(token)) {
                // 액세스 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                Authentication authentication = tokenUtil.getAuthentication(token);

                // SecurityContext 에 Authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 리프레시 토큰이 만료 됐으면 재발급
                if (!tokenUtil.hasRefreshToken(email,token)) {
                    tokenUtil.issueRefreshToken(token);
                }
            }
        } catch (ExpiredJwtException e) {
            //토큰의 유효기간 만료
            // redis에서 access token을 key로 갖는 refresh token이 있는지 확인합니다
            if (tokenUtil.hasRefreshToken(email,token)) {
                logger.info("refresh token 존재. access token 재발급");

                // 리프레시 토큰이 유효하면 액세스 토큰으로부터 유저 정보를 받아옵니다.
                Authentication authentication = tokenUtil.createAuthFromExpiredToken(token);

                // 액세스 토큰 재발급
                String newToken = tokenUtil.createAccessToken((CustomUserDetails) authentication.getPrincipal());

                // 리프레시 토큰 키값 변경
                tokenUtil.renameRefreshToken(email, newToken);

                // cookie에 저장
                Cookie accessCookie = tokenUtil.convertAccessTokenAsCookie(newToken);
                response.addCookie(accessCookie);

                // SecurityContext 에 Authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                request.setAttribute(JwtProperties.EXCEPTION_STRING, JwtErrorCode.EXPIRED_TOKEN);
            }
        } catch (JwtException e) {
            //유효하지 않은 토큰
            request.setAttribute(JwtProperties.EXCEPTION_STRING, JwtErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            request.setAttribute(JwtProperties.EXCEPTION_STRING, JwtErrorCode.COOKIE_NOT_EXIST);
        } catch (NoSuchElementException e){
            request.setAttribute(JwtProperties.EXCEPTION_STRING, JwtErrorCode.TOKEN_NOT_EXIST);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}