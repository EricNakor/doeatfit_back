package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.CustomUserDetails;
import com.Duo960118.fitow.entity.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
// @RequiredArgsConstructor는 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
@Controller
public class UserController {

    // 회원가입
    @GetMapping("join")
    public String join(@ModelAttribute("joinRequest") UserDto.JoinRequestDto joinRequest) {
        return "user/join";
    }

    // 회원탈퇴
    @GetMapping("my-page/withdraw")
    public String withdraw() {
        return "user/withdraw";
    }

    // 로그인
    // spring security가 다해줌
    @GetMapping("login")
    public String login() {
        return "user/login";
    }

//     마이 페이지 및 간단한 회원정보
//    @GetMapping("myPage")
//    public String myPage(HttpServletRequest req) {
//        req.setAttribute("contentPage", "/user/myPage.html");
//        return "user/myPage";
//      SetAttribute 대신 model.Attribute로 변경
//      Spring Boot 형식으로 작성하고자

    // 마이페이지
    @GetMapping("my-page")
    public String myPage(@AuthenticationPrincipal CustomUserDetails customUserDetails,  Model model) {
        // todo: spring security interceptor 공부할때 어떤 방향으로 할지 정하기
        // 현재 인증된 유저 정보가 없을 때??
        if (customUserDetails == null) {
            return "unExpectedError";
        }
        UserDto.UserInfoDto userInfo = customUserDetails.getUserInfo();
        // 프로필 이미지 있는지 확인해
        if (userInfo.getProfileImg() == null) {
            // 없으면 기본 이미지로 profileImg 설정
            userInfo.setProfileImg("default.png");
        }

        model.addAttribute("userInfo", userInfo);
        return "user/myPage";
    }

    // 이메일 찾기
    @GetMapping("find/email")
    public String findEmail(@ModelAttribute("findEmailRequest") UserDto.FindEmailRequestDto findEmailRequest) {
        return "user/findEmail";
    }

    // 가입정보 찾기
    @GetMapping("find/info")
    public String findUserInfo(@ModelAttribute("findUserInfoRequest") UserDto.FindUserInfoRequestDto findUserInfoRequest) {
        return "user/findUserInfo";
    }

    // 신고 및 문의
    @GetMapping("my-page/contacts")
    public String contact() {
        return "user/contacts";
    }

    // 신고 및 문의 자세히 보기
    @GetMapping("my-page/contacts/{id}")
    public String showContactDetail() {
        return "user/contactDetail";
    }

//     프로필
    @GetMapping("my-page/profile")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (customUserDetails == null) {
            return "unExpectedError";
        }
        model.addAttribute("userInfo", customUserDetails.getUserInfo());
        return "user/editProfile";
    }

    // 식단 및 운동 기록
    // 아직 구현하지 않음
    @GetMapping("my-page/history")
    public String history() {
        return "user/history";
    }

}
