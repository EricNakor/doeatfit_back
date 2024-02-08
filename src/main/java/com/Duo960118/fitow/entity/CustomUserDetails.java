package com.Duo960118.fitow.entity;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//정보 객체로 사용되는 객체는 UserDetails를 구현한다
//왜? loadUserByUsername메서드의 반환 타입이 UserDetails이기 때문이다.
//UserEntity에 UserDetails를 직접 구현하면 도메인 객체는 특정 기술에 종속되지 않도록 개발하는 Best Practice(모범 사례)에 위반하게 된다.

// 로그인 할 때 사용
// Spring Security http request에 담긴 정보와 db 정보 비교
public class CustomUserDetails implements UserDetails, CredentialsContainer {
    // UserDetails에 필수적인 것
    private final String email;
    private String passwd;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private Collection<? extends GrantedAuthority> authorities;
    private final String nickName;
    private final String name;
    private final boolean gender;
    private final LocalDate birth;
    private final LocalDate joinDate;
    private final LocalDate passwdEditDate;
    private final String profileImg;

    public CustomUserDetails(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.passwd = userEntity.getPasswd();
        this.nickName = userEntity.getNickName();
        this.name = userEntity.getName();
        this.gender = userEntity.isGender();
        this.birth = userEntity.getBirth();
        this.joinDate = userEntity.getJoinDate();
        this.passwdEditDate = userEntity.getPasswdEditDate();
        this.profileImg = userEntity.getProfileImg();

        // User Role 기반으로 권한 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().getValue()));
        this.authorities = authorities;
    }

    // 마이페이지에 넘길 정보
    public UserDto.UserInfoDto getUserInfo() {
        UserDto.UserInfoDto userInfo = new UserDto.UserInfoDto();
        userInfo.setNickName(this.nickName);
        userInfo.setName(this.name);
        userInfo.setEmail(this.getUsername());
        userInfo.setGender(this.gender);
        userInfo.setBirth(this.birth);
        userInfo.setJoinDate(this.joinDate);
        userInfo.setPasswdEditDate(this.passwdEditDate);
        userInfo.setProfileImg(this.profileImg);
        return userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.passwd;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void eraseCredentials() {
        this.passwd = null;
    }
}
