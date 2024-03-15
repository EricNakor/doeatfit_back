package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.CustomUserDetails;
import com.Duo960118.fitow.entity.GenderEnum;
import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.service.ReportService;
import com.Duo960118.fitow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
// @RequiredArgsConstructor는 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
@Controller
public class UserController {
    //private final ReportFacade reportFacade;
    private final UserService userService;
    private final ReportService reportService;

    // 회원가입
    @GetMapping("join")
    public String join(@ModelAttribute("joinRequest") UserDto.JoinRequestDto joinRequest, Model model) {
        // 성별 enum 설정
        model.addAttribute("userGenderEnums", GenderEnum.values());
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
    public String findEmail(@ModelAttribute("findEmailRequest") UserDto.FindEmailRequestDto findEmailRequest, Model model) {
        // 성별 enum 설정
        model.addAttribute("userGenderEnums", GenderEnum.values());
        return "user/findEmail";
    }

    // 가입정보 찾기
    @GetMapping("find/info")
    public String findUserInfo(@ModelAttribute("findUserInfoRequest") UserDto.FindUserInfoRequestDto findUserInfoRequest, Model model) {
        // 성별 enum 설정
        model.addAttribute("userGenderEnums", GenderEnum.values());
        return "user/findUserInfo";
    }

    // 프로필
    @GetMapping("my-page/profile")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (customUserDetails == null) {
            return "unExpectedError";
        }
        model.addAttribute("userInfo", customUserDetails.getUserInfo());
        return "user/editProfile";
    }

    // 신고 및 문의
    @GetMapping("my-page/reports")
    public String reportList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<ReportDto.ReportInfoDto> reportList = reportService.getReports(customUserDetails.getUsername(), pageRequest);
        model.addAttribute("reports", reportList);
        return "report/reports";
    }

    // 신고 및 문의 자세히 보기
    @GetMapping("my-page/reports/{uuid}")
    public String reportDetail(@PathVariable("uuid") UUID uuid, Model model) {
        model.addAttribute("reportDetail", reportService.getReportDetail(uuid));
        return "report/reportDetail";
    }

    // 신고 및 문의 작성
    @GetMapping("my-page/reports/post")
    public String postReport(@ModelAttribute("report") ReportDto.PostReportRequestDto report, Model model) {
        model.addAttribute("categoryEnums", ReportEntity.ReportCategoryEnum.values());

        return "report/reportPost";
    }
}
