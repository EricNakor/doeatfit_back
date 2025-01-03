package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// 확장된 구현체를 만들어야 할 경우가 발생할 것 같을때
// test code
public interface UserService {
    // 회원가입
    @Transactional
    UserDto.JoinResponseDto join(UserDto.JoinRequestDto joinRequest);

    // 회원탈퇴
    @Transactional
    void withdraw(UserDto.WithDrawRequestDto withDrawRequest);

    // 해당 이메일을 db 에서 찾기
    @Transactional
    UserEntity findByEmail(String email);

    // 이메일 찾기
    @Transactional
    UserDto.EmailListResponseDto findEmail(UserDto.FindEmailRequestDto findEmailRequest);

    // 가입정보 찾기
    @Transactional
    UserDto.FindUserInfoResponseDto findUserInfo(UserDto.FindUserInfoRequestDto userInfoDto);

    // 이메일 중복확인
    @Transactional
    UserDto.CheckDuplicateResponseDto checkEmail(UserDto.CheckEmailRequestDto checkEmailRequest);

    // 닉네임 중복확인
    @Transactional
    UserDto.CheckDuplicateResponseDto checkNickName(UserDto.CheckNickNameRequestDto checkNickNameRequest);

    // 비밀번호 수정
    @Transactional
    UserDto.EditPasswdResponseDto editPasswd(UserDto.EditPasswdRequestDto editPwRequest);

    // 닉네임 수정
    @Transactional
    UserDto.EditNickNameResponseDto editNickName(UserDto.EditNickNameRequestDto editNickNameRequest);

    // 프로필 이미지 이름 수정
    @Transactional
    void editProfileImgName(String email, String profileImg);

    // 닉네임으로 UserEntity 찾기
    @Transactional
    UserEntity findByNickName(String nickName);

    // 모든 유저 조회
    @Transactional
    Page<UserDto.UserInfoDto> getAllUser(Pageable pageable);

    // 이메일로 유저 하나 디테일 조회
    @Transactional
    UserDto.UserInfoDto getUserInfo(String email);

    // 만나이 계산
    int calculateAge(LocalDate birth);

    // 만나이 계산 + 성별
    @Transactional
    UserDto.AgeGenderResponseDto getUserAgeGender(UserDto.AgeGenderRequestDto ageGenderRequest);
}