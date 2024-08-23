package com.Duo960118.fitow.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class EmailDto {

    // 인증번호 이메일 발송 요청
    @Getter
    @Setter
    public static class SendAuthEmailRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
    }

    // 인증번호 인증 요청
    @Getter
    @Setter
    public static class VerifyEmailRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
        @NotBlank(message = "{NotBlank.authNum}")
        @Size(message = "{Size.authNum}")
        private String authNum;
    }
}