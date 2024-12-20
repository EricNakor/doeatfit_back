package com.Duo960118.fitow.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SecurityService extends UserDetailsService {

    // email값으로 회원정보 찾아 customUserDetail 만들어서 반환
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    // 인증서 새로 발급
    Authentication createNewAuthentication(Authentication currentAuth, String email);

    // 수정된 UserEntity로 AuthToken 생성 후 SecurityContext에 삽입
    void syncAuthenticationUser();
}
