package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.annotaion.File;
import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.UserFacade;
import com.Duo960118.fitow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
// RestController == ResponseBody + Controller
// RestController는 JSON 형식(ResponseBody)으로 자동으로 반환
public class UserApiController {
    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);
    private final UserFacade userFacade;
    private final UserService userService;

    // 회원가입
    @PostMapping("users/join")
    public ApiResponse<UserDto.JoinResponseDto> join(@Valid @RequestBody UserDto.JoinRequestDto joinRequest) {
        UserDto.JoinResponseDto joinResponse = userService.join(joinRequest);
        return ApiResponse.success(joinResponse);
    }

    // 이메일 중복확인
    // todo: 중복 체크 인증 여부는 front에서 체크하고, api 우회 접근 방어 로직은 back에서 >> front 작업 시 진행
    @GetMapping("users/check/duplicate/email")
    public ApiResponse<UserDto.CheckDuplicateResponseDto> checkEmailDuplication(@Email(message = "{Email.email}") @NotBlank(message = "{NotBlank.email}") @RequestParam("email") String email ) {
        UserDto.CheckEmailRequestDto checkEmailRequest = new UserDto.CheckEmailRequestDto(email);
        return ApiResponse.success(userService.checkEmail(checkEmailRequest));
    }

    // 닉네임 중복확인
    @GetMapping("users/check/duplicate/nickname")
    public ApiResponse<UserDto.CheckDuplicateResponseDto> checkNickNameDuplication(@Size(min=2,max=12,message = "{Size.nickName}") @NotBlank(message = "{NotBlank.nickName}") @RequestParam("nickName") String nickName ) {
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
    public ApiResponse<UserDto.EditPasswdResponseDto>  editPasswd(@Valid @RequestBody UserDto.EditPasswdRequestDto editPasswdRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        editPasswdRequest.setEmail(customUserDetails.getUsername());
        return ApiResponse.success(userFacade.editPasswd(editPasswdRequest));
    }

    // 닉네임 수정
    @PutMapping("users/nickname")
    public ApiResponse<UserDto.EditNickNameResponseDto> editNickName(@Valid @RequestBody UserDto.EditNickNameRequestDto editNickNameRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        editNickNameRequest.setEmail(customUserDetails.getUsername());
        return ApiResponse.success(userFacade.editNickName(editNickNameRequest));
    }

    // 이메일 찾기
    @PostMapping("users/find/email")
    public ApiResponse<UserDto.EmailListResponseDto> findEmail(@Valid @RequestBody UserDto.FindEmailRequestDto findEmailRequest) {
        return ApiResponse.success(userService.findEmail(findEmailRequest));
    }

    // 가입 정보 찾기
    @PostMapping("users/find/info")
    public ApiResponse<UserDto.FindUserInfoResponseDto> findUserInfo(@Valid @RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        return ApiResponse.success(userService.findUserInfo(userInfoRequest));
    }

    // 임시비밀번호 발송
    @PostMapping("users/send-temp-passwd")
    public ApiResponse<Object> sendTempPasswd(@Valid @RequestBody UserDto.FindUserInfoRequestDto userInfoRequest) {
        userFacade.sendTempPasswd(userInfoRequest.getEmail());
        return ApiResponse.success(null);
    }

    // todo: fileService 할래 말래?? notice 또는 이후 확장될 board등..
    // 프로필 이미지 저장하기
    @PostMapping("users/profile-img")
    public ApiResponse<UserDto.SaveProfileImgResponseDto> saveProfileImg(@File(allowedFileExt = {"jpg", "jpeg", "png"}, fileSizeLimit = 1024 * 1024 * 5) @RequestPart("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        return ApiResponse.success(userFacade.saveProfileImg(customUserDetails.getUsername(), new UserDto.SaveProfileImgRequestDto(file)));
    }

    // 프로필 이미지 조회
    @GetMapping("users/profile-img/{filename}")
    public ApiResponse<Resource> loadProfileImg(@PathVariable("filename") String filename) {
        return ApiResponse.successResource(userFacade.loadProfileImg(filename));
    }

    // 유저 정보
    @GetMapping("users/my-info")
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
    public ApiResponse<UserDto.UserInfoDto> getUserInfo(@Valid @PathVariable("email") String email){
        UserDto.UserInfoDto userInfo = userService.getUserInfo(email);

        if (userInfo.getProfileImg() == null) {
            // 없으면 기본 이미지로 profileImg 설정
            userInfo.setProfileImg("default.png");
        }

        return ApiResponse.success(userInfo);
    }

    // 역할 수정
    @PutMapping("def-cms/users/role")
    public ApiResponse<UserDto.EditUserRoleResponseDto> editUserRole(@Valid @RequestBody UserDto.EditUserRoleRequestDto editUserRoleRequest) {
        UserDto.EditUserRoleResponseDto Response = userFacade.editUserRole(editUserRoleRequest);
        return ApiResponse.success(Response);
    }

    //    // 성별 Enum
//    @GetMapping("users/gender-enum")
//    public ApiResponse<GenderEnum[]> getGenderEnum() {
//        return ApiResponse.success(GenderEnum.values());
//    }
//
//    // 역할 enum
//    @GetMapping("users/role-enum")
//    public ApiResponse<UserEntity.UserRoleEnum[]> getUserRoleEnum(){
//        return ApiResponse.success(UserEntity.UserRoleEnum.values());
//    }

}
