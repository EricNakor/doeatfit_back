package com.Duo960118.fitow.service;

import com.Duo960118.fitow.component.TokenUtil;
import com.Duo960118.fitow.config.UploadConfig;
import com.Duo960118.fitow.entity.JwtProperties;
import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.entity.UserEntity;
import com.Duo960118.fitow.exception.PasswordNotMatchesException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static com.Duo960118.fitow.config.PasswordConfig.generateTempPasswd;

@RequiredArgsConstructor
@Service
public class UserFacade {
    private static final Logger log = LoggerFactory.getLogger(UserFacade.class);
    private final UploadConfig uploadConfig;
    private final SecurityService securityService;
    private final EmailSendService emailSendService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final ReportService reportService;
    private final NoticeService noticeService;
    private final CalculatorService calculatorService;


    // 임시 비밀번호 저장하기
    @Transactional
    public void sendTempPasswd(String email) {
        // 이메일 존재하는지 확인
        UserEntity userEntity = userService.findByEmail(email);

        // 임시 비밀번호로 수정
        // 임시 비밀번호 생성기
        int tempPasswdSpLength = 1;
        int tempPasswdNumLength = 2;
        int tempPasswdAllLength = 9;
        String passwd = generateTempPasswd(tempPasswdSpLength, tempPasswdAllLength, tempPasswdNumLength);
        userEntity.updatePasswd(passwordEncoder.encode(passwd));

        // 요청에서 받은 이메일로 임시 비밀번호 발송
        emailSendService.sendTempPasswd(email, passwd);
    }

    // 닉네임 수정
    @Transactional
    public UserDto.EditNickNameResponseDto editNickName(UserDto.EditNickNameRequestDto editNickNameRequest) {
        UserDto.EditNickNameResponseDto editNickNameResponse = userService.editNickName(editNickNameRequest);
        // 토큰 리프레시
        securityService.syncAuthenticationUser();

        return editNickNameResponse;
    }

    // 프로필 이미지 저장
    @Transactional
    public UserDto.SaveProfileImgResponseDto saveProfileImg(String email, UserDto.SaveProfileImgRequestDto editProfileImgRequest) throws IOException {
        // 파일명 추출
        String fileName = editProfileImgRequest.getProfileImgFile().getOriginalFilename();
        // 파일 확장자 추출
        String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
        String profileImgName;

        // 이메일 존재 여부 확인
        UserEntity user = userService.findByEmail(email);

        // 있으면 프로필 사진이 null 값인지 확인해
        if (user.getProfileImg() == null) {
            // 프로필사진이 null이면 uuid를 발생하고 파일명으로 쓰기
            UUID uuid = UUID.randomUUID();

            // uuid로 이미지 이름 설정 및 파일 확장자 붙이기
            profileImgName = uuid + fileExt;
        } else {
            // 이전 이미지 삭제
            File file = new File(uploadConfig.getProfileImgDir() + File.separator + user.getProfileImg());

            if (!file.delete()) {
                // 예외: 프로필 이미지 삭제 실패
                throw new IOException(user.getProfileImg() + " 해당 프로필 이미지를 삭제할 수 없습니다");
            }

            // 프로필사진이 !null이면 원래 uuid로 파일명 덮어씌우기
            String uuid = user.getProfileImg().substring(0, user.getProfileImg().lastIndexOf("."));
            profileImgName = uuid + fileExt;
        }

        // 프로필 이미지 이름 업데이트
        userService.editProfileImgName(email, profileImgName);

        // 지정된 경로에 저장
//        File file = new File(uploadConfig.getProfileImgDir() + "\\" + profileImgName);
        File file = new File(uploadConfig.getProfileImgDir() + File.separator + profileImgName);
        editProfileImgRequest.getProfileImgFile().transferTo(file);

        // contextHolder.authentication 주입
        securityService.syncAuthenticationUser();

        return new UserDto.SaveProfileImgResponseDto(profileImgName);
    }

    // 비밀번호 수정
    public UserDto.EditPasswdResponseDto editPasswd(UserDto.EditPasswdRequestDto editPasswdRequest) {
        // 수정
        UserDto.EditPasswdResponseDto editPasswdResponse = userService.editPasswd(editPasswdRequest);

        // contextHolder.authentication 주입
        securityService.syncAuthenticationUser();

        return editPasswdResponse;
    }

    // 프로필 이미지 불러오기
    public Resource loadProfileImg(String filename) {
        return new FileSystemResource(uploadConfig.getProfileImgDir() + filename);
    }

    // 회원탈퇴
    public void withdraw(UserDto.WithDrawRequestDto withdrawRequest) throws PasswordNotMatchesException {
        UserEntity user = userService.findByEmail(withdrawRequest.getEmail());

        // DB 삭제
        userService.withdraw(withdrawRequest);

        // 프로필 이미지 삭제
        if (user.getProfileImg() != null) {
            File file = new File(uploadConfig.getProfileImgDir() + File.separator + user.getProfileImg());
            if (file.delete()) {
                log.info("Profile image deleted");
            } else {
                log.warn("Profile image not deleted");
            }
        }

        // 토큰 블랙리스트 추가 or
        // 쿠키에서 토큰 삭제는 해도 되고 안해도 되고
        // 탈퇴한 회원 accessToken 추출
        String accessToken = tokenUtil.resolveToken(withdrawRequest.getHttpServletRequest(), JwtProperties.ACCESS_TOKEN_KEY);
        String email = withdrawRequest.getEmail();

        // refresh token 제거
        tokenUtil.deleteRefreshToken(email);

        // access token blacklist 추가
        tokenUtil.blacklistAccessToken(accessToken);

        // 외부키 끊어주기
        reportService.updateForeignKeysNull(user.getUserId());
        noticeService.updateForeignKeysNull(user.getUserId());
        calculatorService.updateForeignKeysNull(user.getUserId());

    }

    // 유저 롤 수정
    public UserDto.EditUserRoleResponseDto editUserRole(UserDto.EditUserRoleRequestDto editUserRoleRequest) {
        UserEntity userEntity = userService.findByEmail(editUserRoleRequest.getEmail());
        userEntity.updateUserRole(UserEntity.UserRoleEnum.fromString(editUserRoleRequest.getNewUserRole()));

        securityService.syncAuthenticationUser();

        return new UserDto.EditUserRoleResponseDto(userEntity.getRole());
    }
}
