package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.StatusResponseDto;
import com.Duo960118.fitow.entity.EmailDto;
import com.Duo960118.fitow.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EmailApiController {
    private final EmailSendService emailSendService;

    // 인증번호 이메일 발송
    @PostMapping("/email/send/auth")
    public StatusResponseDto sendMail(@RequestBody EmailDto.SendAuthEmailRequestDto sendAuthMailRequest) {
        return new StatusResponseDto(emailSendService.sendAuthEmail(sendAuthMailRequest));
    }
    //todo:프론트 작업 시 [인증하기]버튼 클릭 시 alert 말고 spen으로 노출되게, {blank} >>클릭>> 발송중 >> 발송완료

    // 인증번호 인증
    @GetMapping("/email/verify")
    public StatusResponseDto verify(@RequestParam("email") String email, @RequestParam("authNum") String authNum) {
        EmailDto.VerifyEmailRequestDto verifyEmailRequest = new EmailDto.VerifyEmailRequestDto();
        verifyEmailRequest.setEmail(email);
        verifyEmailRequest.setAuthNum(authNum);
        return new StatusResponseDto(emailSendService.verifyEmail(verifyEmailRequest));
    }
}
