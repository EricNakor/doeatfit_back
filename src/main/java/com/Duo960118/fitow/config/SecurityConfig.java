package com.Duo960118.fitow.config;

import com.Duo960118.fitow.component.CustomAuthenticationEntryPoint;
import com.Duo960118.fitow.component.CustomLogoutHandler;
import com.Duo960118.fitow.component.CustomAccessDeniedHandler;
import com.Duo960118.fitow.entity.JwtProperties;
import com.Duo960118.fitow.filter.JwtFilter;
import com.Duo960118.fitow.filter.LoginFilter;
import com.Duo960118.fitow.component.TokenUtil;
import com.Duo960118.fitow.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// todo: Back-end 유효성 검사 추가
@Configuration
// 스프링의 환경 설정 파일임을 의미하는 어노테이션
@EnableWebSecurity
@RequiredArgsConstructor
// 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션 이를 사용하면 활성화 하는 역할을 함
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomLogoutHandler customLogoutHandler;
    private final TokenUtil tokenUtil;

    // 필터(ex. jwt)를 거치치 않아도 되는 url
    public static final String[] CSS = {"/css/**"};
    public static final String FAVICON = "/favicon.ico";
    public static final String[] JS = {"/js/**"};
    public static final String[] ERROR = {"/error"};
    public static final String[] API = {"/api/home-contents","/api/users/join","/api/def-cms/users","/api/users/find/**"};

    // 인증이 필요없는 url
    public static final String[] NO_AUTH_PATHS = {"/", "/login", "/find/**", "/join", "/notices/**", "/workouts/**", "/test"};
    public static final String[] NO_AUTH_API_PATHS = {"api/users/find/**", "/api/users/send-temp-passwd", "/api/users/join",
            "/api/email/send/auth", "/api/email/verify", "/api/users/check/**",
            "/api/workouts/**", "/api/notices/**", "/api/users/gender-enum",
            "/api/home-contents", "/api/verify-turnstile"};

    // admin 권한이 필요한 url
    public static final String[] ADMIN_AUTH_REQUIRED_PATHS = {"/def-cms/**"};
    public static final String[] ADMIN_AUTH_REQUIRED_API_PATHS = {"/api/def-cms/**"};

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // css,js 같은 resource들은 필터 무시하게 함
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(ERROR)
                .requestMatchers(JS)
                .requestMatchers(CSS)
                .requestMatchers(FAVICON)
                .requestMatchers(API);
    }

    @Bean
    // 스프링 시큐리티의 세부 설정
    // 스프링에 의해 생성 또는 관리되는 객체를 의미하는 어노테이션. (컨트롤러/서비스/리포지토리 등 모두 빈에 해당)
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        // 내부적으로 SecurityFilterChain 클래스가 동작하여 모든 요청 URL에 이 클래스가 필터로 적용되어
        // URL별로 특별한 설정을 할 수 있게 된다.
        // csrf 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable)

                // cors 설정
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))

                // jwt를 사용하므로 생략
                .formLogin(AbstractHttpConfigurer::disable)

                // 세션 비활성화
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Logout Handler, 로그아웃 시 redis에서 access token 제거 및 blacklist 추가
                .logout(logoutConfigurer ->
                        logoutConfigurer
                                .deleteCookies(JwtProperties.ACCESS_TOKEN_KEY) // 로그아웃 후 해당 쿠키 삭제
                                .addLogoutHandler(customLogoutHandler)
                                .logoutSuccessHandler(customLogoutHandler)
                                .logoutUrl("/logout")
                                .permitAll()
                )

                // LoginFilter
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), tokenUtil), UsernamePasswordAuthenticationFilter.class)

                // JwtFilter
                .addFilterBefore(new JwtFilter(tokenUtil), LoginFilter.class)

                // 일반적인 루트가 아닌 다른 방식으로 요청시 거절,
                // header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다.
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(NO_AUTH_PATHS).permitAll()
                        .requestMatchers(NO_AUTH_API_PATHS).permitAll()

                        // admin 권한 확인 (ex. notice 작성,수정,삭제 시)
                        .requestMatchers(ADMIN_AUTH_REQUIRED_PATHS).hasAuthority(UserEntity.UserRoleEnum.ADMIN.getValue())
                        .requestMatchers(ADMIN_AUTH_REQUIRED_API_PATHS).hasAuthority(UserEntity.UserRoleEnum.ADMIN.getValue())
                        .anyRequest().authenticated())

                // 인가 예외 핸들링
                .exceptionHandling((exceptionConfig) -> exceptionConfig.accessDeniedHandler(customAccessDeniedHandler)
                // jwt 인증 관련 예외처리
                .authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }



    // CORS 설정을 위한 Bean
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        // 아래 설정을 '*'로 변경하여 모든 호스트에서의 연결을 허용할 수 있음
//        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
//        // 허용할 출처 설정
//        corsConfiguration.setAllowedMethods(List.of( "GET"));
//        // 허용할 HTTP 메서드 설정
//        corsConfiguration.setAllowCredentials(true);
//        // 자격 증명을 허용할지 여부 설정
//        corsConfiguration.setAllowedHeaders(List.of("*"));
//        // 허용할 헤더 설정
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        // 모든 경로에 대해 CORS 설정 등록
//        return source;
//    }

}
