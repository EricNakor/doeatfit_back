package com.Duo960118.fitow.entity;

import com.Duo960118.fitow.annotaion.Enum;
import com.Duo960118.fitow.annotaion.File;
import com.Duo960118.fitow.annotaion.Nickname;
import com.Duo960118.fitow.annotaion.Password;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class UserDto {
    // 회원가입 DTO
    @Getter
    @Setter
    public static class JoinRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
//        @Pattern(message = "비밀번호 형식이 올바르지 않습니다.",
//                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$")
        @Password
        @NotBlank(message = "{NotBlank.password}")
        private String passwd;
        @NotBlank(message = "{NotBlank.name}")
        @Size(min=2,max=70, message = "{Size.name}")
        private String name;
        @NotBlank(message = "{NotBlank.nickName}")
        @Nickname
        private String nickName;
        @Enum(enumClass = GenderEnum.class,message = "{Enum.gender}")
        @NotBlank(message = "{NotBlank.gender}")
        private String gender;
        @PastOrPresent(message = "{PastOrPresent.birth}")
        @NotNull(message = "{NotNull.birth}")
        private LocalDate birth;
    }

    // 이메일 찾기 DTO
    @Getter
    @Setter
    public static class FindEmailRequestDto {
        @NotBlank(message = "{NotBlank.name}")
        private String name;
        @Enum(enumClass = GenderEnum.class, message = "{Enum.gender}")
        @NotBlank(message = "{NotBlank.gender}")
        private String gender;
        @PastOrPresent(message = "{PastOrPresent.birth}")
        private LocalDate birth;
    }

    // 비밀번호 수정 DTO
    @Getter
    @Setter
    public static class EditPasswdRequestDto {
        private String email;
        @NotBlank(message = "{NotBlank.password}")
        private String currentPasswd;
        @Password
        @NotBlank(message = "{NotBlank.newPassword}")
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

    // 닉네임 수정 요청
    @Getter
    @Setter
    @Builder
    public static class EditNickNameRequestDto {
        private String email;
        @Nickname
        @NotBlank(message = "{NotBlank.nickName}")
        private String newNickName;
    }

    // 닉네임 수정 응답
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class EditNickNameResponseDto {
        private final String nickName;
    }

    // 프로필사진 수정 요청
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class SaveProfileImgRequestDto {
        @NotNull
        @File(allowedFileExt = {"jpg", "jpeg", "png"}, fileSizeLimit = 1024 * 1024 * 5)
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
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
        @NotBlank(message = "{NotBlank.name}")
        private String name;
        @Enum(enumClass = GenderEnum.class, message = "{Enum.gender}")
        @NotBlank(message = "{NotBlank.gender}")
        private String gender;
        @NotNull(message = "{NotNull.birth}")
        @PastOrPresent(message = "{PastOrPresent.birth}")
        private LocalDate birth;
    }

    @Getter
    @Setter
    public static class SendTempPasswdRequestDto {
        // 내부 클래스 static..? 공부해보기
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
    }

    // Email 찾기 응답
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class EmailListResponseDto {
        private final List<String> emails;
    }

    // 탈퇴 요청
    @Getter
    @Setter
    @Builder
    public static class WithDrawRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private final String email;
        @Password
        @NotBlank(message = "{NotBlank.password}")
        private final String passwd;
        private final HttpServletRequest httpServletRequest;
    }

    // 회원 권한 수정
    @Getter
    @Setter
    public static class EditUserRoleRequestDto {
        @Email(message = "{Email.}email")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
        @Enum(enumClass = UserEntity.UserRoleEnum.class,message = "{Enum.role}")
        @NotBlank(message = "{NotBlank.role}")
        private String newUserRole;
    }

    // 계산 나이, 성별 정보 DTO
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class AgeGenderRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private final String email;
    }

    // 유저 나이, 성별 응답
    @Getter
    @Setter
    @Builder
    public static class AgeGenderResponseDto {
        private final GenderEnum gender;
        private final int age;
    }

    // 회원가입 응답
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

    // 유저 역할 응답
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class EditUserRoleResponseDto {
        private final UserEntity.UserRoleEnum role;
    }

    // 프로필 이미지 응답
    @Getter
    @RequiredArgsConstructor
    public static class SaveProfileImgResponseDto {
        private final String profileImg;
    }

    // 중복확인 응답
    @Getter
    @RequiredArgsConstructor
    public static class CheckDuplicateResponseDto {
        private final Boolean isDuplicated;
    }

    // 이메일 중복 확인 요청
    @Getter
    @RequiredArgsConstructor
    public static class CheckEmailRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private final String email;
    }

    // 닉네임 중복 확인 요청
    @Getter
    @RequiredArgsConstructor
    public static class CheckNickNameRequestDto {
        @NotBlank(message = "{NotBlank.nickName}")
        // 길이, 특수문자
        @Nickname
        private final String nickName;
    }

    // 유저 정보 찾기 응답
    @Getter
    @RequiredArgsConstructor
    public static class FindUserInfoResponseDto {
        private final Boolean isJoined;
    }
}