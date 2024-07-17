package com.Duo960118.fitow.service;

import com.Duo960118.fitow.component.TokenUtil;
import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.entity.UserEntity;
import com.Duo960118.fitow.config.UploadConfig;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


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

    public static String tempPasswdGenerator(int spSize, int allSize, int numSize) {
        final char[] passwdCollectionSpCha = new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        final char[] passwdCollectionNum = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
        final char[] passwdCollectionAll = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        return getRandPasswd(spSize, passwdCollectionSpCha) + getRandPasswd(allSize, passwdCollectionAll) + getRandPasswd(numSize, passwdCollectionNum);
    }

    static String getRandPasswd(int size, char[] passwdCollection) {
        StringBuilder randPasswd = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int selectRandomPasswd = (int) (Math.random() * (passwdCollection.length));
            randPasswd.append(passwdCollection[selectRandomPasswd]);
        }
        return randPasswd.toString();
    }

    // 임시 비밀번호 저장하기
    @Transactional
    public boolean sendTempPasswd(String email) {
        try {
            // 이메일 존재하는지 확인
            UserEntity userEntity = userService.findByEmail(email);

            // 임시 비밀번호로 수정
            // 임시 비밀번호 생성기
            int tempPasswdSpLength = 1;
            int tempPasswdNumLength = 2;
            int tempPasswdAllLength = 9;
            String passwd = tempPasswdGenerator(tempPasswdSpLength, tempPasswdAllLength, tempPasswdNumLength);
            userEntity.updatePasswd(passwordEncoder.encode(passwd));

            // 요청에서 받은 이메일로 임시 비밀번호 발송
            emailSendService.sendTempPasswd(email, passwd);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    // 닉네임 수정
    @Transactional
    public boolean editNickName(String email, UserDto.EditNickNameRequestDto editNickNameRequest) {
        try {
            if (!userService.editNickName(email, editNickNameRequest)) {
                return false;
            }
            // 토큰 리프레시
            securityService.syncAuthenticationUser();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    // 프로필 이미지 저장
    @Transactional
    public boolean saveProfileImg(String email, UserDto.SaveProfileImgRequestDto editProfileImgRequest) {
        try {
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
                File file = new File(uploadConfig.getProfileImgDir() + "\\" + user.getProfileImg());
                if (file.delete()) {
                    log.info("Profile image deleted");
                } else {
                    log.warn("Profile image not deleted");
                }

                // 프로필사진이 !null이면 원래 uuid로 파일명 덮어씌우기
                String uuid = user.getProfileImg().substring(0, user.getProfileImg().lastIndexOf("."));
                profileImgName = uuid + fileExt;
            }

            // 프로필 이미지 이름 업데이트
            if (userService.editProfileImgName(email, profileImgName)) {
                log.info("Profile image updated");
            } else {
                log.warn("Profile image not updated");
            }

            // 지정된 경로에 저장
            File file = new File(uploadConfig.getProfileImgDir() + "\\" + profileImgName);
            editProfileImgRequest.getProfileImgFile().transferTo(file);

            // contextHolder.authentication 주입
            securityService.syncAuthenticationUser();
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    // 비밀번호 수정
    public boolean editPasswd(String email, UserDto.EditPasswdRequestDto editPasswdRequest) {
        try {
            // 수정
            if (!userService.editPasswd(email, editPasswdRequest)) {
                return false;
            }

            // contextHolder.authentication 주입
            securityService.syncAuthenticationUser();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    // 프로필 이미지 불러오기
    public Resource loadProfileImg(String filename) {
        return new FileSystemResource(uploadConfig.getProfileImgDir() + filename);
    }

    // 회원가입
    public boolean join(UserDto.JoinRequestDto joinRequest) {
        return userService.join(joinRequest);
    }

    // 닉네임 중복 확인
    public boolean checkNickName(String nickName) {
        return userService.checkNickName(nickName);
    }

    // 이메일 중복 확인
    public boolean checkEmail(String email) {
        return userService.checkEmail(email);
    }

    // 회원탈퇴
    public boolean withdraw(UserDto.WithDrawDto withdrawRequest) {

        UserEntity user = userService.findByEmail(withdrawRequest.getEmail());

        // 프로필 이미지 삭제
        if (user.getProfileImg() != null) {
            File file = new File(uploadConfig.getProfileImgDir() + "\\" + user.getProfileImg());
            if (file.delete()) {
                log.info("Profile image deleted");
            } else {
                log.warn("Profile image not deleted");
            }
        }

        // 토큰 블랙리스트 추가 or
        // 쿠키에서 토큰 삭제는 해도 되고 안해도 되고
        // 탈퇴한 회원 accessToken 추출
        String accessToken = tokenUtil.resolveToken(withdrawRequest.getHttpServletRequest(), TokenUtil.ACCESS_TOKEN_KEY);
        String email = withdrawRequest.getEmail();

        // refresh token 제거
        tokenUtil.deleteRefreshToken(email);

        // access token blacklist 추가
        tokenUtil.blacklistAccessToken(accessToken);

        // 외부키 끊어주기
        reportService.updateForeinKeysNull(user.getUserId());
        noticeService.updateForeinKeysNull(user.getUserId());
        calculatorService.updateForeinKeysNull(user.getUserId());

        // DB 삭제
        return userService.withdraw(withdrawRequest.getEmail(), withdrawRequest.getPasswd());
    }

    // 비밀번호 변경을 위한 이메일 찾기
    public List<String> findEmail(UserDto.FindEmailRequestDto findEmailRequest) {
        return userService.findEmail(findEmailRequest);
    }

    // 이메일 찾기를 위한 가입정보 대조
    public boolean findUserInfo(UserDto.FindUserInfoRequestDto userInfoRequest) {
        return userService.findUserInfo(userInfoRequest);
    }

    // 유저 롤 수정
    public UserDto.UserInfoDto editUserRole(UserDto.EditUserRoleRequestDto editUserRoleRequest) {
        UserEntity userEntity = userService.findByEmail(editUserRoleRequest.getEmail());
        userEntity.updateUserRole(editUserRoleRequest.getNewUserRole());

        securityService.syncAuthenticationUser();

        return new UserDto.UserInfoDto(
                userEntity.getEmail(),
                userEntity.getNickName(),
                userEntity.getName(),
                userEntity.getGender(),
                userEntity.getBirth(),
                userEntity.getJoinDate(),
                userEntity.getPasswdEditDate(),
                userEntity.getProfileImg(),
                userEntity.getRole()
        );
    }
}
