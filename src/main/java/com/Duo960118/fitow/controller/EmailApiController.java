package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.EmailDto;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.EmailSendService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailApiController {
    private final EmailSendService emailSendService;

    // 인증번호 이메일 발송
    @PostMapping("/send/auth")
    public ApiResponse<Object> sendMail(@Valid @RequestBody EmailDto.SendAuthEmailRequestDto sendAuthMailRequest) {
        emailSendService.sendAuthEmail(sendAuthMailRequest);
        return ApiResponse.success(null);
    }
    //todo:프론트 작업 시 [인증하기]버튼 클릭 시 alert 말고 spen으로 노출되게, {blank} >>클릭>> 발송중 >> 발송완료

    // 인증번호 인증
    @GetMapping("/verify")
    public ApiResponse<Object> verify(@Email(message = "{Email.email}") @NotBlank(message = "{NotBlank.email}") @RequestParam("email") String email,
                                      @Size(message = "{Size.authNum}", min = 6, max = 6) @NotBlank(message = "{NotBlank.authNum}") @RequestParam("authNum") String authNum) {
        EmailDto.VerifyEmailRequestDto verifyEmailRequest = new EmailDto.VerifyEmailRequestDto();
        verifyEmailRequest.setEmail(email);
        verifyEmailRequest.setAuthNum(authNum);
        emailSendService.verifyEmail(verifyEmailRequest);
        return ApiResponse.success(null);
    }
}
