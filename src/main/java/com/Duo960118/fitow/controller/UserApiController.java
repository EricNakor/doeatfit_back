package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.service.UserFacade;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
// RestController == ResponseBody + Controller
// RestController는 JSON 형식(ResponseBody)으로 자동으로 반환
public class UserApiController {
    private final UserFacade userFacade;

    // 회원가입
    @PostMapping("/user/join")
    public StatusResponseDto join(@RequestBody UserDto.JoinRequestDto joinRequest) {
        return new StatusResponseDto(userFacade.join(joinRequest));
    }

    // 이메일 중복확인
    // todo: 중복 체크 인증 여부는 front에서 체크하고, api 우회 접근 방어 로직은 back에서 >> front 작업 시 진행
    @GetMapping("/user/check/duplicate/email")
    public StatusResponseDto checkEmailDuplication(@RequestParam("email") String email) {
        return new StatusResponseDto(userFacade.checkEmail(email));
    }

    // 닉네임 중복확인
    @GetMapping("/user/check/duplicate/nickname")
    public StatusResponseDto checkNickNameDuplication(@RequestParam("nickName") String nickName) {
        return new StatusResponseDto(userFacade.checkNickName(nickName));
    }

    // 회원탈퇴
    @DeleteMapping("/user/withdraw")
    public StatusResponseDto withdraw(@RequestBody UserDto.WithdrawRequestDto withdrawRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails, HttpSession session) {
        // 탈퇴 후 세션 끊기
        session.invalidate();
        withdrawRequest.setEmail(customUserDetails.getUsername());
        return new StatusResponseDto(userFacade.withdraw(withdrawRequest));
    }

    // 비밀번호 수정
    @PutMapping("/user/passwd")
    public StatusResponseDto editPasswd(@RequestBody UserDto.EditPasswdRequestDto editPasswdRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new StatusResponseDto(userFacade.editPasswd(customUserDetails.getUsername(), editPasswdRequest));
    }

    // 닉네임 수정
    @PutMapping("/user/nickname")
    public StatusResponseDto editNickName(@RequestBody UserDto.EditNickNameRequestDto editNickNameRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new StatusResponseDto(userFacade.editNickName(customUserDetails.getUsername(), editNickNameRequest));
    }

    // 이메일 찾기
    @PostMapping("/user/find/email")
    public UserDto.EmailDto findEmail(@RequestBody UserDto.FindEmailRequestDto findEmailRequest) {
        return new UserDto.EmailDto(userFacade.findEmail(findEmailRequest));
    }

    // 가입 정보 찾기
    @PostMapping("/user/find/info")
    public StatusResponseDto findUserInfo(@RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        return new StatusResponseDto(userFacade.findUserInfo(userInfoRequest));
    }

    // 임시비밀번호 발송
    @PostMapping("/user/send-temp-passwd")
    public StatusResponseDto sendTempPasswd(@RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        return new StatusResponseDto(userFacade.sendTempPasswd(userInfoRequest.getEmail()));
    }

    // todo: fileService 할래 말래?? notice 또는 이후 확장될 board등..
    // 프로필 이미지 저장하기
    @PostMapping("/user/profile-img")
    public StatusResponseDto saveProfileImg(@RequestPart("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new StatusResponseDto(userFacade.saveProfileImg(customUserDetails.getUsername(), new UserDto.SaveProfileImgRequestDto(file)));
    }

    // 프로필 이미지 가져오기
    @GetMapping("/user/profile-img/{filename}")
    public Resource loadProfileImg(@PathVariable("filename") String filename) {
        return userFacade.loadProfileImg(filename);
    }

}
