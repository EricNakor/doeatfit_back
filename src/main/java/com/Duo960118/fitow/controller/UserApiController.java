package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.UserFacade;
import com.Duo960118.fitow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
// RestController == ResponseBody + Controller
// RestController는 JSON 형식(ResponseBody)으로 자동으로 반환
public class UserApiController {
    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);
    private final UserFacade userFacade;
    private final UserService userService;

    // 회원가입
    @PostMapping("users/join")
    public ApiResponse<UserDto.JoinResponseDto> join(@RequestBody @Valid UserDto.JoinRequestDto joinRequest) {
        // validate
        // ok -> success , not ok -> fail
        // @Valid -> 예외
        // 전역 예외 핸들러 -> 거기서 ApiResponse.fail();

        UserDto.JoinResponseDto joinResponse = userService.join(joinRequest);

        return ApiResponse.success(joinResponse);
    }

    // 이메일 중복확인
    // todo: 중복 체크 인증 여부는 front에서 체크하고, api 우회 접근 방어 로직은 back에서 >> front 작업 시 진행
    @GetMapping("users/check/duplicate/email")
    public ApiResponse<UserDto.CheckDuplicateDto> checkEmailDuplication(@RequestParam("email") String email ) {
        UserDto.CheckEmailRequestDto checkEmailRequest = new UserDto.CheckEmailRequestDto(email);
        return ApiResponse.success(userService.checkEmail(checkEmailRequest));
    }

    // 닉네임 중복확인
    @GetMapping("users/check/duplicate/nickname")
    public ApiResponse<UserDto.CheckDuplicateDto > checkNickNameDuplication(@RequestParam("nickName") String nickName ) {
        UserDto.CheckNickNameRequestDto checkNickNameRequest = new UserDto.CheckNickNameRequestDto(nickName);
        return ApiResponse.success(userService.checkNickName(checkNickNameRequest));
    }

    // 회원탈퇴
    @DeleteMapping("users/withdraw")
    public ApiResponse<Object>  withdraw(@RequestParam("passwd") String passwd,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      HttpServletRequest httpServletRequest) {
        // 탈퇴 후 세션 끊기
        // jwt 토큰 쓸 때는 필요 없음
        //        session.invalidate();
        // 대신 토큰 블랙 리스트 추가 및 제거

        UserDto.WithDrawRequestDto withDrawRequest =  UserDto.WithDrawRequestDto.builder()
                .email(customUserDetails.getUsername())
                .passwd(passwd)
                .httpServletRequest(httpServletRequest)
                .build();

        userFacade.withdraw(withDrawRequest);
        return ApiResponse.success(null);
    }

    // 비밀번호 수정
    @PutMapping("users/passwd")
    public ApiResponse<UserDto.EditPasswdResponseDto>  editPasswd(@RequestBody UserDto.EditPasswdRequestDto editPasswdRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        editPasswdRequest.setEmail(customUserDetails.getUsername());
        return ApiResponse.success(userFacade.editPasswd(editPasswdRequest));
    }

    // 닉네임 수정
    @PutMapping("users/nickname")
    public ApiResponse<UserDto.EditNickNameResponseDto> editNickName(@RequestBody UserDto.EditNickNameDto editNickNameRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        editNickNameRequest.setEmail(customUserDetails.getUsername());
        return ApiResponse.success(userFacade.editNickName(editNickNameRequest));
    }

    // 이메일 찾기
    @PostMapping("users/find/email")
    public ApiResponse<UserDto.EmailListDto> findEmail(@RequestBody UserDto.FindEmailRequestDto findEmailRequest) {
        return ApiResponse.success(userService.findEmail(findEmailRequest));
    }

    // 가입 정보 찾기
    @PostMapping("users/find/info")
    public ApiResponse<UserDto.FindUserInfoResponseDto> findUserInfo(@RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        return ApiResponse.success(userService.findUserInfo(userInfoRequest));
    }

    // 임시비밀번호 발송
    @PostMapping("users/send-temp-passwd")
    public ApiResponse<Object> sendTempPasswd(@RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        userFacade.sendTempPasswd(userInfoRequest.getEmail());
        return ApiResponse.success(null);
    }

    // todo: fileService 할래 말래?? notice 또는 이후 확장될 board등..
    // 프로필 이미지 저장하기
    @PostMapping("users/profile-img")
    public ApiResponse<UserDto.SaveProfileImgResponseDto> saveProfileImg(@RequestPart("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        return ApiResponse.success(userFacade.saveProfileImg(customUserDetails.getUsername(), new UserDto.SaveProfileImgRequestDto(file)));
    }

    // 프로필 이미지 가져오기
    @GetMapping("users/profile-img/{filename}")
    public ApiResponse<Resource> loadProfileImg(@PathVariable("filename") String filename) {
        return ApiResponse.successResource(userFacade.loadProfileImg(filename));
    }

    // 성별 Enum
    @GetMapping("users/gender-enum")
    public ApiResponse<GenderEnum[]> getGenderEnum() {
        return ApiResponse.success(GenderEnum.values());
    }

    // 역할 enum
    @GetMapping("users/role-enum")
    public ApiResponse<UserEntity.UserRoleEnum[]> getUserRoleEnum(){
        return ApiResponse.success(UserEntity.UserRoleEnum.values());
    }

    // 유저 정보
    @GetMapping("users/info")
    public ApiResponse<UserDto.UserInfoDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserDto.UserInfoDto userInfo = customUserDetails.getUserInfo();
        // 프로필 이미지 있는지 확인해
        if (userInfo.getProfileImg() == null) {
            // 없으면 기본 이미지로 profileImg 설정
            userInfo.setProfileImg("default.png");
        }
        return ApiResponse.success(userInfo);
    }

    // 유저 만나이, 성별 계산
    @GetMapping("users/age-gender")
    public ApiResponse<UserDto.AgeGenderResponseDto> getUserAge(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserDto.AgeGenderRequestDto ageGenderRequest = new UserDto.AgeGenderRequestDto(customUserDetails.getUsername());
        return ApiResponse.success(userService.getUserAgeGender(ageGenderRequest));
    }

    // 모든 유저정보 가져오기
    @GetMapping("def-cms/users")
    public ApiResponse<List<UserDto.UserInfoDto>> getUserInfos() {
        List<UserDto.UserInfoDto> users =  userService.getAllUser();

        return ApiResponse.success(users);
    }

    // 유저 자세히 보기
    @GetMapping("def-cms/users/{email}")
    public ApiResponse<UserDto.UserInfoDto> getUserInfo(@PathVariable("email") String email){
        UserDto.UserInfoDto userInfo = userService.getUserInfo(email);

        if (userInfo.getProfileImg() == null) {
            // 없으면 기본 이미지로 profileImg 설정
            userInfo.setProfileImg("default.png");
        }

        return ApiResponse.success(userInfo);
    }

    // 역할 수정
    @PutMapping("def-cms/users/role")
    public ApiResponse<UserDto.EditUserRoleResponseDto> editUserRole(@RequestBody UserDto.EditUserRoleRequestDto editUserRoleRequest) {
        UserDto.EditUserRoleResponseDto Response = userFacade.editUserRole(editUserRoleRequest);
        return ApiResponse.success(Response);
    }
}
