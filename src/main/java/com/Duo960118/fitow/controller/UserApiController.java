package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.service.UserFacade;
import com.Duo960118.fitow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
// RestController == ResponseBody + Controller
// RestController는 JSON 형식(ResponseBody)으로 자동으로 반환
public class UserApiController {
    private final UserFacade userFacade;
    private final UserService userService;


    // 회원가입
    @PostMapping("users/join")
    public StatusResponseDto join(@RequestBody UserDto.JoinRequestDto joinRequest) {
        return new StatusResponseDto(userFacade.join(joinRequest));
    }

    // 이메일 중복확인
    // todo: 중복 체크 인증 여부는 front에서 체크하고, api 우회 접근 방어 로직은 back에서 >> front 작업 시 진행
    @GetMapping("users/check/duplicate/email")
    public StatusResponseDto checkEmailDuplication(@RequestParam("email") String email) {
        return new StatusResponseDto(userFacade.checkEmail(email));
    }

    // 닉네임 중복확인
    @GetMapping("users/check/duplicate/nickname")
    public StatusResponseDto checkNickNameDuplication(@RequestParam("nickName") String nickName) {
        return new StatusResponseDto(userFacade.checkNickName(nickName));
    }

    // 회원탈퇴
    @DeleteMapping("users/withdraw")
    public StatusResponseDto withdraw(@RequestParam("passwd") String passwd,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      HttpServletRequest httpServletRequest) {
        // 탈퇴 후 세션 끊기
        // jwt 토큰 쓸 때는 필요 없음
        //        session.invalidate();
        // 대신 토큰 블랙 리스트 추가 및 제거

        UserDto.WithDrawDto withDrawDto = new UserDto.WithDrawDto();
        withDrawDto.setEmail(customUserDetails.getUsername());
        withDrawDto.setPasswd(passwd);
        withDrawDto.setHttpServletRequest(httpServletRequest);

        return new StatusResponseDto(userFacade.withdraw(withDrawDto));
    }

    // 비밀번호 수정
    @PutMapping("users/passwd")
    public StatusResponseDto editPasswd(@RequestBody UserDto.EditPasswdRequestDto editPasswdRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new StatusResponseDto(userFacade.editPasswd(customUserDetails.getUsername(), editPasswdRequest));
    }

    // 닉네임 수정
    @PutMapping("users/nickname")
    public StatusResponseDto editNickName(@RequestBody UserDto.EditNickNameRequestDto editNickNameRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new StatusResponseDto(userFacade.editNickName(customUserDetails.getUsername(), editNickNameRequest));
    }

    // 이메일 찾기
    @PostMapping("users/find/email")
    public UserDto.EmailDto findEmail(@RequestBody UserDto.FindEmailRequestDto findEmailRequest) {
        return new UserDto.EmailDto(userFacade.findEmail(findEmailRequest));
    }

    // 가입 정보 찾기
    @PostMapping("users/find/info")
    public StatusResponseDto findUserInfo(@RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        return new StatusResponseDto(userFacade.findUserInfo(userInfoRequest));
    }

    // 임시비밀번호 발송
    @PostMapping("users/send-temp-passwd")
    public StatusResponseDto sendTempPasswd(@RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        return new StatusResponseDto(userFacade.sendTempPasswd(userInfoRequest.getEmail()));
    }

    // todo: fileService 할래 말래?? notice 또는 이후 확장될 board등..
    // 프로필 이미지 저장하기
    @PostMapping("users/profile-img")
    public StatusResponseDto saveProfileImg(@RequestPart("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new StatusResponseDto(userFacade.saveProfileImg(customUserDetails.getUsername(), new UserDto.SaveProfileImgRequestDto(file)));
    }

    // 프로필 이미지 가져오기
    @GetMapping("users/profile-img/{filename}")
    public Resource loadProfileImg(@PathVariable("filename") String filename) {
        return userFacade.loadProfileImg(filename);
    }

    // 성별 Enum
    @GetMapping("users/gender-enum")
    public ResponseEntity<GenderEnum[]> getGenderEnum() {
        return ResponseEntity.ok().body(GenderEnum.values());
    }

    // 역할 enum
    @GetMapping("users/role-enum")
    public ResponseEntity<UserEntity.UserRoleEnum[]> getUserRoleEnum(){
        return ResponseEntity.ok().body(UserEntity.UserRoleEnum.values());
    }

    // 유저 정보
    @GetMapping("users/info")
    public ResponseEntity<UserDto.UserInfoDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserDto.UserInfoDto userInfo = customUserDetails.getUserInfo();
        // 프로필 이미지 있는지 확인해
        if (userInfo.getProfileImg() == null) {
            // 없으면 기본 이미지로 profileImg 설정
            userInfo.setProfileImg("default.png");
        }
        return ResponseEntity.ok().body(userInfo);
    }

    // 유저 만나이, 성별 계산
    @GetMapping("users/age-gender")
    public ResponseEntity<UserDto.AgeGenderResponseDto> getUserAge(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        int age = userService.calculateAge(customUserDetails.getUserInfo().getBirth());
        GenderEnum gender = customUserDetails.getUserInfo().getGender();

        UserDto.AgeGenderResponseDto responseDto = new UserDto.AgeGenderResponseDto(gender, age);

        return ResponseEntity.ok().body(responseDto);
    }

    // 모든 유저정보 가져오기
    @GetMapping("def-cms/users")
    public ResponseEntity<List<UserDto.UserInfoDto>> getUserInfos() {
        List<UserDto.UserInfoDto> users =  userService.getAllUser();

        return ResponseEntity.ok().body(users);
    }

    // 유저 자세히 보기
    @GetMapping("def-cms/users/{email}")
    public ResponseEntity<UserDto.UserInfoDto> getUserInfo(@PathVariable("email") String email){
        UserDto.UserInfoDto userInfo = userService.getUserInfo(email);

        if (userInfo.getProfileImg() == null) {
            // 없으면 기본 이미지로 profileImg 설정
            userInfo.setProfileImg("default.png");
        }

        return ResponseEntity.ok().body(userInfo);
    }


    // 역할 수정
    @PutMapping("def-cms/users/role")
    public ResponseEntity<UserDto.UserInfoDto> editUserRole(@RequestBody UserDto.EditUserRoleRequestDto editUserRoleRequest) {
        return ResponseEntity
                .ok()
                .body(userFacade.editUserRole(editUserRoleRequest));
    }
}
