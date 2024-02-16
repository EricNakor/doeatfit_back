package com.Duo960118.fitow.entity;

import lombok.Getter;
import lombok.Setter;

public class EmailDto {

    // 인증번호 이메일 발송 요청
    @Getter
    @Setter
    public static class SendAuthEmailRequestDto {
        private String email;
    }

    // 인증번호 인증 요청
    @Getter
    @Setter
    public static class VerifyEmailRequestDto {
        private String email;
        private String authNum;

    }
}
