package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 확장된 구현체를 만들어야 할 경우가 발생할 것 같을때
// test code
public interface UserService {
    // 회원가입
    @Transactional
    boolean join(UserDto.JoinRequestDto joinRequest);

    // 회원탈퇴
    @Transactional
    boolean withdraw(String email, String passwd);

    // 해당 이메일을 db 에서 찾기
    @Transactional
    UserEntity findByEmail(String email);

    // 이메일  찾기
    @Transactional
    List<String> findEmail(UserDto.FindEmailRequestDto findEmailRequest);

    // 가입정보 찾기
    @Transactional
    boolean findUserInfo(UserDto.FindUserInfoRequestDto userInfoDto);

    // 이메일 중복확인
    @Transactional
    boolean checkEmail(String email);

    // 닉네임 중복확인
    @Transactional
    boolean checkNickName(String nickName);

    // 비밀번호 수정
    @Transactional
    boolean editPasswd(String email, UserDto.EditPasswdRequestDto editPwRequest);

    // 닉네임 수정
    @Transactional
    boolean editNickName(String email, UserDto.EditNickNameRequestDto editNickNameRequest);

    // 프로필 이미지 이름 수정
    @Transactional
    boolean editProfileImgName(String email, String profileImg);

    // 닉네임으로 UserEntity 찾기
    @Transactional
    UserEntity findByNickName(String nickName);
}
