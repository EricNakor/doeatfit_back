package com.Duo960118.fitow.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class UserDto {

    // 회원가입 DTO
    @Getter
    @Setter
    public static class JoinRequestDto {
        private String email;
        private String passwd;
        private String name;
        private String nickName;
        private GenderEnum gender;
        private LocalDate birth;
    }

    // 이메일 찾기 DTO
    @Getter
    @Setter
    public static class FindEmailRequestDto {
        private String name;
        private GenderEnum gender;
        private LocalDate birth;
    }

    // 비밀번호 수정 DTO
    @Getter
    @Setter
    public static class EditPasswdRequestDto {
        private String currentPasswd;
        private String newPasswd;
    }

    // 닉네임 수정 DTO
    @Getter
    @Setter
    public static class EditNickNameRequestDto {
        private String newNickName;
    }

    // 프로필사진 수정 DTO
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class SaveProfileImgRequestDto {
        private final MultipartFile profileImgFile;
        // record가 뭔지 찾아보자
    }

    // 마이페이지 DTO
    @Getter
    @Setter
    public static class UserInfoDto {
        private String email;
        private String nickName;
        private String name;
        private GenderEnum gender;
        private LocalDate birth;
        private LocalDate joinDate;
        private LocalDate passwdEditDate;
        private String profileImg;
    }

    // 가입정보 찾기 DTO
    @Getter
    @Setter
    public static class FindUserInfoRequestDto {
        // 내부 클래스 static..? 공부해보기
        private String email;
        private String name;
        private GenderEnum gender;
        private LocalDate birth;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class EmailDto {
        private final List<String> emails;
    }

    @Getter
    @Setter
    public static class WithdrawRequestDto {
        private String email;
        private String passwd;
    }
}
