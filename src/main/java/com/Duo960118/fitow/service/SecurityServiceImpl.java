package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.UserEntity;
import com.Duo960118.fitow.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserService userService;

    // email값으로 회원정보 찾아 customUserDetail 만들어서 반환
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findByEmail(email);

        return new CustomUserDetails(userEntity);
    }


    // 인증서 새로 발급
    @Override
    public Authentication createNewAuthentication(Authentication currentAuth, String email) {
        UserDetails newPrincipal = loadUserByUsername(email);

        UsernamePasswordAuthenticationToken newAuthToken = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());

        newAuthToken.setDetails(currentAuth.getDetails());
        return newAuthToken;
    }






    // 수정된 UserEntity로 AuthToken 생성 후 SecurityContext에 삽입
    @Override
    public void syncAuthenticationUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // 2-2. 현재 Authentication로 사용자 인증 후 새 Authentication 정보를 SecurityContextHolder에 세팅
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication, customUserDetails.getUsername()));
    }
}