package com.Duo960118.fitow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
// 스프링의 환경 설정 파일임을 의미하는 어노테이션
@EnableWebSecurity
// 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션 이를 사용하면 활성화 하는 역할을 함
public class SecurityConfig {
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/예외처리하고 싶은 url", "/예외처리하고 싶은 url");
//    }
    @Bean
    // 스프링 시큐리티의 세부 설정
    // 스프링에 의해 생성 또는 관리되는 객체를 의미하는 어노테이션. (컨트롤러/서비스/리포지토리 등 모두 빈에 해당)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 내부적으로 SecurityFilterChain 클래스가 동작하여 모든 요청 URL에 이 클래스가 필터로 적용되어
        // URL별로 특별한 설정을 할 수 있게 된다.
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/my-page/**", "api/user/nickname", "/api/user/passwd", "/api/user/profile-img/", "api/user/profile-img").authenticated()
                .requestMatchers("/user/my-page/withdraw","api/user/withdraw").authenticated()
                .anyRequest().permitAll())
            .formLogin((formLogin) -> formLogin
                .usernameParameter("email")
                .passwordParameter("passwd")
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/my-page"))
            // 스프링 시큐리티 로그인 설정 담당
            // 로그인 페이지 URL과 로그인 성공 시 이동할 페이지 설정
            .logout((logout) -> logout
                // 회원 탈퇴 시 로그아웃
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
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
