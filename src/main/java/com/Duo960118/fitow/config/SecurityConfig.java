package com.Duo960118.fitow.config;

import com.Duo960118.fitow.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

// todo: 기능 구현 완료 후 JWT 도입 필요
// todo: Back-end 유효성 검사 추가
@Configuration
// 스프링의 환경 설정 파일임을 의미하는 어노테이션
@EnableWebSecurity
// 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션 이를 사용하면 활성화 하는 역할을 함
public class SecurityConfig {
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/예외처리하고 싶은 url", "/예외처리하고 싶은 url");
//    }

    String[] noAuthGet = {"/", "/login", "/find/**", "/join", "/notices/**", "/api/email/verify", "/api/user/check/**", "/api/notices/**"};
    String[] noAuth = {"api/user/find/**", "/api/user/send-temp-passwd", "/api/user/join", "/api/email/send/auth"};

    // admin 권한이 필요한 url
    String[] adminAuthorityRequired = {"/api/notices/**","/notices/post","/notices/edit"};

    @Bean
    // 스프링 시큐리티의 세부 설정
    // 스프링에 의해 생성 또는 관리되는 객체를 의미하는 어노테이션. (컨트롤러/서비스/리포지토리 등 모두 빈에 해당)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 내부적으로 SecurityFilterChain 클래스가 동작하여 모든 요청 URL에 이 클래스가 필터로 적용되어
        // URL별로 특별한 설정을 할 수 있게 된다.
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, noAuthGet).permitAll()
                        .requestMatchers(noAuth).permitAll()
                        .requestMatchers("/css/**","/js/**","/error").permitAll()
                        )
                .formLogin((formLogin) -> formLogin
                        .usernameParameter("email")
                        .passwordParameter("passwd")
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/my-page",true))
                // 스프링 시큐리티 로그인 설정 담당
                // 로그인 페이지 URL과 로그인 성공 시 이동할 페이지 설정
                .logout((logout) -> logout
                        // 회원 탈퇴 시 로그아웃
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        // notice 작성,수정,삭제 시 admin 권한 확인
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(adminAuthorityRequired).hasAuthority(UserEntity.UserRoleEnum.ADMIN.getValue())
                );

        // 위에서 필터된 이외의 주소는 인가 되었는지만 확인
        http
                .authorizeHttpRequests((authorize)->authorize
                        .anyRequest().authenticated()
                );

        // 로그아웃 설정 담당
        return http.build();
    }

    // 비밀번호 인코더
    // Bean을 만드는 가장 쉬운 방법으로 @Configuration이 적용된 파일에 메서드를 새로 추가하는 것.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
