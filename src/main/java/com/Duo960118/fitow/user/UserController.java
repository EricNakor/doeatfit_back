package com.Duo960118.fitow.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    // 회원 가입
    @GetMapping("signIn")
    public String signIn()
    {
        return "signIn";
    }

    // 회원 탈퇴
    @GetMapping("signOut")
    public String signOut()
    {
        return "signOut";
    }

    // 로그인
    @GetMapping("logIn")
    public String logIn()
    {
        return "login";
    }

    // 로그아웃
    @GetMapping("logOut")
    public String logOut()
    {
        return "logout";
    }

    // 마이 페이지
    @GetMapping("myPage")
    public String myPage()
    {
        return "myPage";
    }

    // 신고 및 문의
    @GetMapping("contact")
    public String contact()
    {
        return "contact";
    }

    // 신고 및 문의 자세히 보기
    @GetMapping("contactDetail")
    public String contactDetail()
    {
        return "contactDetail";
    }

    // 신고 및 문의 게시
    @GetMapping("postContact")
    public String postContact()
    {
        return "postContact";
    }

    // 프로필 수정
    @GetMapping("editProfile")
    public String editProfile()
    {
        return "editProfile";
    }

    // 비밀번호 수정
    @GetMapping("editPw")
    public String editPw()
    {
        return "editPw";
    }

    // 닉네임 수정
    @GetMapping("editNickName")
    public String editNickName()
    {
        return "editNickName";
    }

    // 프로필 이미지 수정
    @GetMapping("editProfileImg")
    public String editProfileImg()
    {
        return "editProfileImg";
    }
}
