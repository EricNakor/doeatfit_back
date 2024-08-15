package com.Duo960118.fitow.entity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class UserDto {

    // 회원가입 DTO
    @Getter
    @Setter
    public static class JoinRequestDto {
        // @Valid 실패할 경우  MethodArgumentNotValidException 예외 발생
        //출처: https://mangkyu.tistory.com/174 [MangKyu's Diary:티스토리]
        @Email(message = "이메일 오류")
        private String email;

        @Pattern(message = "비밀번호 오류", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$")
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
        private String email;
        private String currentPasswd;
        private String newPasswd;
    }
    @Getter
    @RequiredArgsConstructor
    public static class EditPasswdResponseDto {
        private final LocalDate passwdEditDate;
    }

    @Getter
    @Builder
    public static class EditUserInfoResponseDto {
        private Long userId;
    }

    // 닉네임 수정 DTO
    @Getter
    @Setter
    @Builder
    public static class EditNickNameDto {
        private String email;
        private String newNickName;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class EditNickNameResponseDto {
        private final String nickName;
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDto {
        private String email;
        private String nickName;
        private String name;
        private GenderEnum gender;
        private LocalDate birth;
        private LocalDate joinDate;
        private LocalDate passwdEditDate;
        private String profileImg;
        private UserEntity.UserRoleEnum role;
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

    // Email 찾기 DTO
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class EmailListDto {
        private final List<String> emails;
    }

    // 탈퇴 DTO
    @Getter
    @Setter
    @Builder
    public static class WithDrawRequestDto {
        private final String email;
        private final String passwd;
        private final HttpServletRequest httpServletRequest;
    }

    // 회원 권한 DTO
    @Getter
    @Setter
    public static class EditUserRoleRequestDto {
        private String email;
        private UserEntity.UserRoleEnum newUserRole;
    }

    // 계산 나이, 성별 정보 DTO
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class AgeGenderRequestDto {
        private final String email;
    }

    @Getter
    @Setter
    @Builder
    public static class AgeGenderResponseDto {
        private final GenderEnum gender;
        private final int age;
    }

    @Getter
    @Builder
    public static class JoinResponseDto {
        private Long userId;
        private String email;
        private String nickName;
        private String name;
        private GenderEnum gender;
        private LocalDate birth;
        private LocalDate joinDate;
        private LocalDate passwdEditDate;
//        private String profileImg;
//        private UserEntity.UserRoleEnum role;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class EditUserRoleResponseDto{
        private final UserEntity.UserRoleEnum role;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SaveProfileImgResponseDto {
        private final String profileImg;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CheckDuplicateDto {
        private final Boolean isDuplicated;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CheckEmailRequestDto {
        private final String email;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CheckNickNameRequestDto {
        private final String nickName;
    }

    @Getter
    @RequiredArgsConstructor
    public static class FindUserInfoResponseDto {
        private final Boolean isJoined;
    }
}