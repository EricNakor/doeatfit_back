package com.Duo960118.fitow.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    // 회원 가입
    @GetMapping("signIn")
    public String signIn()
    {
        return "user/signIn";
    }

    // 회원 탈퇴
    @GetMapping("signOut")
    public String signOut()
    {
        return "user/signOut";
    }

    // 로그인
    @GetMapping("logIn")
    public String logIn()
    {
        return "user/login";
    }

    // 마이 페이지
    @GetMapping("myPage")
    public String myPage(HttpServletRequest req)
    {
        req.setAttribute("contentPage","/user/myPage.html");
        return "user/myPage";
    }

    // 신고 및 문의
    @GetMapping("myPage/contact")
    public String contact()
    {
        return "user/contact";
    }

    // 신고 및 문의 자세히 보기
    @GetMapping("myPage/contactDetail")
    public String contactDetail()
    {
        return "user/contactDetail";
    }

    // 프로필
    @GetMapping("myPage/profile")
    public String profile()
    {
        return "user/profile";
    }

    // 프로필 수정
    @GetMapping("myPage/editProfile")
    public String editProfile()
    {
        return "user/editProfile";
    }

    // history
    @GetMapping("myPage/history")
    public String history()
    {
        return "user/history";
    }

}
