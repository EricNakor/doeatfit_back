package com.Duo960118.fitow.component;

import com.Duo960118.fitow.entity.BlackListedToken;
import com.Duo960118.fitow.entity.JwtProperties;
import com.Duo960118.fitow.entity.TokensEntity;
import com.Duo960118.fitow.repository.TokenBlackListRepository;
import com.Duo960118.fitow.repository.TokensRepository;
import com.Duo960118.fitow.service.SecurityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class TokenUtil {
    private final SecurityService securityService;
    private final TokensRepository tokensRepository;
    private final TokenBlackListRepository accessTokenBlackListRepository;
    private final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public TokenUtil(SecurityService securityService, TokensRepository tokensRepository, TokenBlackListRepository tokenBlackListRepository) {
        this.securityService = securityService;
        this.tokensRepository = tokensRepository;
        this.accessTokenBlackListRepository = tokenBlackListRepository;
    }

    /**
     * 토큰의 Claim 디코딩
     */
    private Claims getAllClaims(String token) {
        token = token.split(" ")[1].trim();
        return Jwts
                .parser()
                .verifyWith(JwtProperties.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 만료된 access token에서 유저 정보를 가져올 때 사용
    //Headers: {"alg":"HS256","typ":"JWT","exp":"1300819380"}  [0]
    //Payload: {"email":"John Doe","role":"user","admin":true}   [1]
    public Authentication createAuthFromExpiredToken(String expiredToken) throws JsonProcessingException {
        String username = new ObjectMapper().readValue(
                new String(Base64.getDecoder().decode(expiredToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class).get("email").toString();
        UserDetails userDetails = securityService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // jwt 라이브러리 사용 하지 않고 (토큰 유효성 검사 x) 직접 base64로 디코딩하여 토큰에 있는 유저 이메일 파싱
    public String getEmailFromAccessToken(String expiredToken) throws JsonProcessingException {
        return new ObjectMapper().readValue(
                new String(Base64.getDecoder().decode(expiredToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class).get("email").toString();
    }

    public String getUsername(String token) {
        logger.debug(getAllClaims(token).get("email", String.class));
        return getAllClaims(token).get("email", String.class);
    }


    // email을 key, access token과 refresh token을 value로 하여 redis에 저장
    // refresh 토큰에는 사용자 정보를 담을 필요가 없다.
    public void issueRefreshToken(String accessToken) {
        String refreshToken = UUID.randomUUID().toString();
        TokensEntity tokensEntity = TokensEntity.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .email(this.getUsername(accessToken))
                .ttl(JwtProperties.REFRESH_TOKEN_TIME)
                .build();

        // email 필요
        tokensRepository.save(tokensEntity);
        //setDataExp(REFRESH_TOKEN_PREFIX + accessToken, refreshToken, Duration.ofSeconds(REFRESH_TOKEN_TIME));
    }

    // 입력 받은 email을 key 값으로 갖는 TokensEntity가 access token 값으로 입력 받은 access token과 같은 refresh token이 있는 확인 
    public boolean hasRefreshToken(String email, String accessToken) {
        TokensEntity tokensEntity; //hasKey(REFRESH_TOKEN_PREFIX + accessToken);
        try {
            tokensEntity = tokensRepository.findById(email).orElseThrow(() -> new RuntimeException("리프레시 토큰 없음"));
        } catch (RuntimeException e) {
            return false;
        }
        return tokensEntity.getAccessToken().equals(accessToken);
    }

    // 로그 아웃 시 사용
    public void deleteRefreshToken(String email) {
        tokensRepository.deleteById(email); //deleteData(REFRESH_TOKEN_PREFIX + accessToken);
    }

    public void blacklistAccessToken(String accessToken) {
        BlackListedToken blackListedToken = BlackListedToken.builder()
                .accessToken(accessToken)
                .ttl(JwtProperties.ACCESS_TOKEN_TIME).build();
        accessTokenBlackListRepository.save(blackListedToken); //setDataExp(BLACKLIST_PREFIX + accessToken, "", Duration.ofSeconds(ACCESS_TOKEN_TIME));
    }

    // 입력 받은 email을 key값으로 하는 TokensEntity에서 access token을 반환
    public String findAccessToken(String email) {
        TokensEntity tokensEntity = tokensRepository.findById(email).orElseThrow(() -> new UsernameNotFoundException("비로그인 이메일"));
        return tokensEntity.getAccessToken();
    }

    // jwt access token 생성
    public String createAccessToken(UserDetails userDetails) {
        return JwtProperties.TOKEN_PREFIX + Jwts.builder()
                .claim("email", userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_TIME))
                .signWith(JwtProperties.secretKey)
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = securityService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Request의 Cookie에서 token 값을 가져옴. "refresh_token" : "TOKEN값"
    public String resolveToken(HttpServletRequest request, String tokenKey) {
        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키 없음");
        }

        String token = Arrays.stream(cookies).filter((c) -> c.getName().equals(tokenKey)).findFirst().orElse(new Cookie(tokenKey, "")).getValue();
        if(token.isEmpty()){
            throw new NoSuchElementException();
        }

        return URLDecoder.decode(token, StandardCharsets.UTF_8);
    }


    // bearer 확인 + 만료일자 확인 + 로그아웃 블랙리스트에 있는지 확인
    public boolean validateAccessToken(String bearerToken) {
        // 텍스트 있는지 확인
        if (!StringUtils.hasText(bearerToken)) {
            throw new JwtException("access token이 없습니다");
        }

        // Bearer 검증
        if (!bearerToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new JwtException("유효하지 않은 bearer token"); // message :"Jwt token 유효성 검증 실패: {}",
        }

        // 블랙 리스트 확인
        if (accessTokenBlackListRepository.existsById(bearerToken)) {
            throw new JwtException("bearer token이 블랙리스트에 있음"); // message : "access token이 블랙리스트에 있음"
        }

        // 액세스 토큰 추출
        String token = bearerToken.substring(JwtProperties.TOKEN_PREFIX.length());

        // 토큰에서 claim 추출
        Claims claims = Jwts.parser()
                .setSigningKey(JwtProperties.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 유효기간 확인
        Date expiration = claims.getExpiration();
        logger.info("expired at: {}",expiration);

        if(expiration.before(new Date())){
            // 쿠키를 사용해서 임시로 빈 헤더 넘겨줌
            throw new ExpiredJwtException(Jwts.header().build(), claims, expiration.toString());
        }

        return true;
    }

    // access token을 cookie로 변환
    public Cookie convertAccessTokenAsCookie(String accessToken) {
        Cookie cookie = new Cookie(JwtProperties.ACCESS_TOKEN_KEY, URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setHttpOnly(true);

        cookie.setSecure(false);  // todo: 나중에 ssl인증서를 사용할 때는 true 로 설정하여 https 통신이 아닌 경우에는 cookie 저장 안되게 해야함
        return cookie;
    }

    // 어떤 유저에 대해 이미 로그인이 된 상태에서 새로운 위치에서 로그인 할 경우, access token을 새로 발급한 후 
    // redis에서 이 유저의 email을 key 값으로 갖는 TokensEntity를 찾아 access token을 새로 발급한 access token으로 업데이트
    // 중복 로그인 방지와 access token이 만료됐지만 refresh token이 존재할 때 사용함
    public void renameRefreshToken(String email, String newToken) {
        TokensEntity tokensEntity = tokensRepository.findById(email).orElseThrow(() -> new RuntimeException("해당 이메일을 키로 갖는 tokensEntity 존재 안함"));
        tokensEntity.updateAccessToken(newToken);
        tokensRepository.save(tokensEntity); //renameKey(REFRESH_TOKEN_PREFIX + oldToken, REFRESH_TOKEN_PREFIX + newToken);
    }

    // 중복 로그인 체크
    public boolean isLogin(String email) {
        return tokensRepository.existsById(email);
    }
}